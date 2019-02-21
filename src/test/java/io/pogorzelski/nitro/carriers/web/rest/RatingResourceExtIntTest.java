package io.pogorzelski.nitro.carriers.web.rest;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;
import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.domain.Person;
import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.domain.enumeration.CargoType;
import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.repository.UserRepository;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
import io.pogorzelski.nitro.carriers.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static io.pogorzelski.nitro.carriers.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the RatingResourceExt REST controller.
 *
 * @see RatingResourceExt
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NitroCarriersApp.class)
public class RatingResourceExtIntTest {

    private static final String DEFAULT_POSTAL_CODE = "15-111";
    private static final String UPDATED_POSTAL_CODE = "99-999";

    private static final CargoType DEFAULT_CARGO_TYPE = CargoType.FTL_13_6;
    private static final CargoType UPDATED_CARGO_TYPE = CargoType.EXTRA_13_6;

    private static final Double DEFAULT_DISTANCE = 1D;
    private static final Double UPDATED_DISTANCE = 2D;

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

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingExtService mockRatingExtService;

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
        final RatingResourceExt ratingResourceExt = new RatingResourceExt(mockRatingExtService);
        doReturn(userRepository.findOneByLogin("user").get()).when(mockRatingExtService).getUser();
        this.restRatingMockMvc = MockMvcBuilders.standaloneSetup(ratingResourceExt)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rating createEntity(EntityManager em) {

        Carrier carrier = CarrierResourceIntTest.createEntity(em);
        Person person = PersonResourceIntTest.createEntity(em);
        Country country = CountryResourceIntTest.createEntity(em);
        em.persist(country);
        em.flush();

        return new Rating()
            .distance(DEFAULT_DISTANCE)
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
            .cargoType(DEFAULT_CARGO_TYPE);
    }

    @Before
    public void initTest() {
        rating = createEntity(em);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void createRating() throws Exception {
        int databaseSizeBeforeCreate = ratingRepository.findAll().size();

        // Create the Rating
        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isCreated());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeCreate + 1);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getChargePostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testRating.getDischargePostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testRating.getDistance()).isEqualTo(DEFAULT_DISTANCE);
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

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
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

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
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

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDistanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ratingRepository.findAll().size();
        // set the field null
        rating.setDistance(null);

        // Create the Rating, which fails.

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
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

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
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

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
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

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
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

        restRatingMockMvc.perform(post("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isBadRequest());

        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updateRatingSuccess() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        // Disconnect from session so that the updates on updatedRating are not directly saved in db
        em.detach(updatedRating);
        updatedRating
            .chargePostalCode(UPDATED_POSTAL_CODE)
            .dischargePostalCode(UPDATED_POSTAL_CODE)
            .cargoType(UPDATED_CARGO_TYPE)
            .distance(UPDATED_DISTANCE)
            .contact(UPDATED_CONTACT)
            .price(UPDATED_PRICE)
            .flexibility(UPDATED_FLEXIBILITY)
            .recommendation(UPDATED_RECOMMENDATION)
            .average(UPDATED_AVERAGE);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andDo(print())
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getChargePostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testRating.getDischargePostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testRating.getCargoType()).isEqualTo(UPDATED_CARGO_TYPE);
        assertThat(testRating.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testRating.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testRating.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRating.getFlexibility()).isEqualTo(UPDATED_FLEXIBILITY);
        assertThat(testRating.getRecommendation()).isEqualTo(UPDATED_RECOMMENDATION);
        assertThat(testRating.getAverage()).isEqualTo(UPDATED_AVERAGE);
        assertThat(testRating.getCarrier().getId()).isEqualTo(ratingDB.getCarrier().getId());
        assertThat(testRating.getPerson().getId()).isEqualTo(ratingDB.getPerson().getId());

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails("admin")
    public void updateRatingFailWrongUser() throws Exception {
        // Initialize the database
        mockRatingExtService.save(rating);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andDo(print())
            .andExpect(status().is(403));
    }

    @Test
    @Transactional
    public void updateNonExistingRating() throws Exception {
        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rating)))
            .andExpect(status().isBadRequest());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(0)).save(rating);
    }

    @Test
    @Transactional
    @WithUserDetails("admin")
    public void deleteRatingFailWrongUser() throws Exception {
        // Initialize the database
        mockRatingExtService.save(rating);

        // Try to delete the rating
        restRatingMockMvc.perform(delete("/api/ext/ratings/{id}", rating.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().is(403));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void deleteRatingSuccess() throws Exception {
        // Initialize the database
        mockRatingExtService.save(rating);

        int databaseSizeBeforeDelete = ratingRepository.findAll().size();

        // Delete the rating
        restRatingMockMvc.perform(delete("/api/ext/ratings/{id}", rating.getId())
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
}
