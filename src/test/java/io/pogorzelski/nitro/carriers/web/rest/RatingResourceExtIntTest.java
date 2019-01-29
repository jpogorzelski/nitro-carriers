package io.pogorzelski.nitro.carriers.web.rest;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;
import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
import io.pogorzelski.nitro.carriers.service.dto.RatingExtDTO;
import io.pogorzelski.nitro.carriers.service.mapper.RatingExtMapper;
import io.pogorzelski.nitro.carriers.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static io.pogorzelski.nitro.carriers.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
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
        Address address = new Address()
            .country(country)
            .postalCode(DEFAULT_POSTAL_CODE);

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
            .chargeAddress(address)
            .dischargeAddress(address)
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
        assertThat(testRating.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testRating.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRating.getFlexibility()).isEqualTo(DEFAULT_FLEXIBILITY);
        assertThat(testRating.getRecommendation()).isEqualTo(DEFAULT_RECOMMENDATION);
        assertThat(testRating.getAverage()).isEqualTo(DEFAULT_AVERAGE);
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
