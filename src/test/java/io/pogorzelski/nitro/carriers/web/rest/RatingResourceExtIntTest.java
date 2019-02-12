package io.pogorzelski.nitro.carriers.web.rest;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;
import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
import io.pogorzelski.nitro.carriers.service.dto.RatingDTO;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;
import io.pogorzelski.nitro.carriers.service.mapper.RatingExtMapper;
import io.pogorzelski.nitro.carriers.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static io.pogorzelski.nitro.carriers.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RatingResourceExt REST controller.
 *
 * @see RatingResourceExt
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NitroCarriersApp.class)
public class RatingResourceExtIntTest {

    private static final String DEFAULT_CHARGE_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CHARGE_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DISCHARGE_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DISCHARGE_POSTAL_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONTACT = 1;
    private static final Integer UPDATED_CONTACT = 2;

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final Integer DEFAULT_FLEXIBILITY = 1;
    private static final Integer UPDATED_FLEXIBILITY = 2;

    private static final Grade DEFAULT_RECOMMENDATION = Grade.DEF_YES;
    private static final Grade UPDATED_RECOMMENDATION = Grade.YES;

    private static final Double DEFAULT_AVERAGE = 1D;
    private static final Double UPDATED_AVERAGE = 2D;

    private static final String DEFAULT_POSTAL_CODE = "15-111";

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingExtMapper ratingExtMapper;

    @Autowired
    private RatingExtService ratingExtService;

    /**
     * This repository is mocked in the io.pogorzelski.nitro.carriers.repository.search test package.
     *
     * @see io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepositoryMockConfiguration
     */
    @Autowired
    private RatingSearchRepository mockRatingSearchRepository;


    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRatingMockMvc;

    private Rating rating;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RatingResourceExt ratingResourceExt = new RatingResourceExt(ratingExtService);
        this.restRatingMockMvc = MockMvcBuilders.standaloneSetup(ratingResourceExt)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createEntity(EntityManager em) {

        Carrier carrier = CarrierResourceIntTest.createEntity(em);
        Person person = PersonResourceIntTest.createEntity(em)
            .carrier(carrier);
        Country country = CountryResourceIntTest.createEntity(em);
        em.persist(country);
        em.flush();

        CargoType cargoType = CargoTypeResourceIntTest.createEntity(em);
        em.persist(cargoType);
        em.flush();

        return new Rating()
            .contact(DEFAULT_CONTACT)
            .price(DEFAULT_PRICE)
            .flexibility(DEFAULT_FLEXIBILITY)
            .recommendation(DEFAULT_RECOMMENDATION)
            .average(DEFAULT_AVERAGE)
            .carrier(carrier)
            .person(person)
            .chargeCountry(country)
            .chargePostalCode(DEFAULT_POSTAL_CODE)
            .dischargeCountry(country)
            .dischargePostalCode(DEFAULT_POSTAL_CODE)
            .cargoType(cargoType);
    }

    @Before
    public void initTest() {
        rating = createEntity(em);
    }

    @Test
    @Transactional
    public void createRating() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);
        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isCreated());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate + 1);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getChargePostalCode()).isEqualTo(DEFAULT_CHARGE_POSTAL_CODE);
        assertThat(testRating.getDischargePostalCode()).isEqualTo(DEFAULT_DISCHARGE_POSTAL_CODE);
        assertThat(testRating.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testRating.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRating.getFlexibility()).isEqualTo(DEFAULT_FLEXIBILITY);
        assertThat(testRating.getRecommendation()).isEqualTo(DEFAULT_RECOMMENDATION);
        assertThat(testRating.getAverage()).isEqualTo(DEFAULT_AVERAGE);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    public void createRatingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating with an existing ID
        rating.setId(1L);
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(0)).save(rating);
    }


    @Test
    @Transactional
    public void checkChargePostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setChargePostalCode(null);

        // Create the Rating, which fails.
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDischargePostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setDischargePostalCode(null);

        // Create the Rating, which fails.
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setContact(null);

        // Create the Rating, which fails.
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setPrice(null);

        // Create the Rating, which fails.
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFlexibilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setFlexibility(null);

        // Create the Rating, which fails.
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRecommendationIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setRecommendation(null);

        // Create the Rating, which fails.
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRatings() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        // Get all the ratingList
        restRatingMockMvc.perform(get("/api/ratings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId().intValue())))
            .andExpect(jsonPath("$.[*].chargePostalCode").value(hasItem(DEFAULT_CHARGE_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].dischargePostalCode").value(hasItem(DEFAULT_DISCHARGE_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].flexibility").value(hasItem(DEFAULT_FLEXIBILITY)))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION.toString())))
            .andExpect(jsonPath("$.[*].average").value(hasItem(DEFAULT_AVERAGE.doubleValue())));
    }

    @Test
    @Transactional
    public void getRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        // Get the rating
        restRatingMockMvc.perform(get("/api/ratings/{id}", rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rating.getId().intValue()))
            .andExpect(jsonPath("$.chargePostalCode").value(DEFAULT_CHARGE_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.dischargePostalCode").value(DEFAULT_DISCHARGE_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.flexibility").value(DEFAULT_FLEXIBILITY))
            .andExpect(jsonPath("$.recommendation").value(DEFAULT_RECOMMENDATION.toString()))
            .andExpect(jsonPath("$.average").value(DEFAULT_AVERAGE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRating() throws Exception {
        // Get the rating
        restRatingMockMvc.perform(get("/api/ratings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        // Disconnect from session so that the updates on updatedRating are not directly saved in db
        em.detach(updatedRating);
        updatedRating
            .chargePostalCode(UPDATED_CHARGE_POSTAL_CODE)
            .dischargePostalCode(UPDATED_DISCHARGE_POSTAL_CODE)
            .contact(UPDATED_CONTACT)
            .price(UPDATED_PRICE)
            .flexibility(UPDATED_FLEXIBILITY)
            .recommendation(UPDATED_RECOMMENDATION)
            .average(UPDATED_AVERAGE);
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(updatedRating);

        restRatingMockMvc.perform(put("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getChargePostalCode()).isEqualTo(UPDATED_CHARGE_POSTAL_CODE);
        assertThat(testRating.getDischargePostalCode()).isEqualTo(UPDATED_DISCHARGE_POSTAL_CODE);
        assertThat(testRating.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testRating.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRating.getFlexibility()).isEqualTo(UPDATED_FLEXIBILITY);
        assertThat(testRating.getRecommendation()).isEqualTo(UPDATED_RECOMMENDATION);
        assertThat(testRating.getAverage()).isEqualTo(UPDATED_AVERAGE);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    public void updateNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Create the Rating
        RatingExtDTO ratingExtDTO = ratingExtMapper.toDto(rating);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc.perform(put("/api/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ratingExtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(0)).save(rating);
    }

    @Test
    @Transactional
    public void deleteRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);

        int databaseSizeBeforeDelete = ratingRepository.findAll().size();

        // Delete the rating
        restRatingMockMvc.perform(delete("/api/ratings/{id}", rating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).deleteById(rating.getId());
    }

    @Test
    @Transactional
    public void searchRating() throws Exception {
        // Initialize the database
        ratingRepository.saveAndFlush(rating);
        when(mockRatingSearchRepository.search(queryStringQuery("id:" + rating.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rating), PageRequest.of(0, 1), 1));
        // Search the rating
        restRatingMockMvc.perform(get("/api/_search/ratings?query=id:" + rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId().intValue())))
            .andExpect(jsonPath("$.[*].chargePostalCode").value(hasItem(DEFAULT_CHARGE_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].dischargePostalCode").value(hasItem(DEFAULT_DISCHARGE_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].flexibility").value(hasItem(DEFAULT_FLEXIBILITY)))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION.toString())))
            .andExpect(jsonPath("$.[*].average").value(hasItem(DEFAULT_AVERAGE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rating.class);
        Rating rating1 = new Rating();
        rating1.setId(1L);
        Rating rating2 = new Rating();
        rating2.setId(rating1.getId());
        assertThat(rating1).isEqualTo(rating2);
        rating2.setId(2L);
        assertThat(rating1).isNotEqualTo(rating2);
        rating1.setId(null);
        assertThat(rating1).isNotEqualTo(rating2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RatingExtDTO.class);
        RatingExtDTO ratingExtDTO1 = new RatingExtDTO();
        ratingExtDTO1.setId(1L);
        RatingExtDTO ratingExtDTO2 = new RatingExtDTO();
        assertThat(ratingExtDTO1).isNotEqualTo(ratingExtDTO2);
        ratingExtDTO2.setId(ratingExtDTO1.getId());
        assertThat(ratingExtDTO1).isEqualTo(ratingExtDTO2);
        ratingExtDTO2.setId(2L);
        assertThat(ratingExtDTO1).isNotEqualTo(ratingExtDTO2);
        ratingExtDTO1.setId(null);
        assertThat(ratingExtDTO1).isNotEqualTo(ratingExtDTO2);
    }

   /* @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(ratingExtMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(ratingExtMapper.fromId(null)).isNull();
    }*/
}
