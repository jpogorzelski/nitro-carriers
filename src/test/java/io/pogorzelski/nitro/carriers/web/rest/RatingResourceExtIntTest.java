package io.pogorzelski.nitro.carriers.web.rest;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;
import io.pogorzelski.nitro.carriers.domain.*;
import io.pogorzelski.nitro.carriers.domain.enumeration.CargoType;
import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.repository.UserRepository;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
import io.pogorzelski.nitro.carriers.web.rest.errors.ErrorConstants;
import io.pogorzelski.nitro.carriers.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
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
import java.math.BigDecimal;
import java.util.List;

import static io.pogorzelski.nitro.carriers.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    private static final Boolean DEFAULT_ADD_ALTERNATIVE = false;
    private static final Boolean UPDATED_ADD_ALTERNATIVE = true;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PRICE_PER_KM = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_KM = new BigDecimal(2);

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

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WHITE_LIST = false;
    private static final Boolean UPDATED_WHITE_LIST = true;

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
            .alwaysDo(print())
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
        City city = CityResourceIntTest.createEntity(em);
        Country country = city.getCountry();
        em.persist(city);
        em.flush();

        return new Rating()
            .distance(DEFAULT_DISTANCE)
            .contact(DEFAULT_CONTACT)
            .price(DEFAULT_PRICE)
            .flexibility(DEFAULT_FLEXIBILITY)
            .recommendation(DEFAULT_RECOMMENDATION)
            .average(DEFAULT_AVERAGE)
            .remarks(DEFAULT_REMARKS)
            .carrier(carrier)
            .person(person)
            .addAlternative(DEFAULT_ADD_ALTERNATIVE)
            .altCarrier(carrier)
            .altPerson(person)
            .chargeCountry(country)
            .chargeCity(city)
            .chargePostalCode(DEFAULT_POSTAL_CODE)
            .dischargeCountry(country)
            .dischargeCity(city)
            .dischargePostalCode(DEFAULT_POSTAL_CODE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .pricePerKm(DEFAULT_PRICE_PER_KM)
            .whiteList(DEFAULT_WHITE_LIST)
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
        assertThat(testRating.getRemarks()).isEqualTo(DEFAULT_REMARKS);
        assertThat(testRating.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testRating.getPricePerKm()).isEqualTo(DEFAULT_PRICE_PER_KM);
        assertThat(testRating.isWhiteList()).isEqualTo(DEFAULT_WHITE_LIST);
        assertThat(testRating.isAddAlternative()).isEqualTo(DEFAULT_ADD_ALTERNATIVE);
        assertThat(testRating.getAltCarrier()).isNull();
        assertThat(testRating.getAltPerson()).isNull();

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

        Carrier carrier = new Carrier().name("test").transId(666);
        Person person = new Person().firstName("John").lastName("Doe").carrier(carrier).companyId(44);

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        // Disconnect from session so that the updates on updatedRating are not directly saved in db
        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());
        updatedRating
            .chargePostalCode(UPDATED_POSTAL_CODE)
            .dischargePostalCode(UPDATED_POSTAL_CODE)
            .cargoType(UPDATED_CARGO_TYPE)
            .distance(UPDATED_DISTANCE)
            .contact(UPDATED_CONTACT)
            .price(UPDATED_PRICE)
            .flexibility(UPDATED_FLEXIBILITY)
            .recommendation(UPDATED_RECOMMENDATION)
            .average(UPDATED_AVERAGE)
            .remarks(UPDATED_REMARKS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .pricePerKm(UPDATED_PRICE_PER_KM)
            .whiteList(UPDATED_WHITE_LIST)
            .addAlternative(UPDATED_ADD_ALTERNATIVE)
            .altCarrier(carrier)
            .altPerson(person);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
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
        assertThat(testRating.getRemarks()).isEqualTo(UPDATED_REMARKS);
        assertThat(testRating.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testRating.getPricePerKm()).isEqualTo(UPDATED_PRICE_PER_KM);
        assertThat(testRating.isWhiteList()).isEqualTo(UPDATED_WHITE_LIST);
        assertThat(testRating.getCarrier().getId()).isEqualTo(ratingDB.getCarrier().getId());
        assertThat(testRating.getPerson().getId()).isEqualTo(ratingDB.getPerson().getId());

        assertThat(testRating.isAddAlternative()).isEqualTo(UPDATED_ADD_ALTERNATIVE);
        assertThat(testRating.getAltCarrier().getTransId()).isEqualTo(carrier.getTransId());
        assertThat(testRating.getAltPerson().getCompanyId()).isEqualTo(person.getCompanyId());

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updatePerson() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();

        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());

        String phoneNumber = "660110550";
        Person person = updatedRating.getPerson()
            .firstName("Jan")
            .lastName("Kowalski")
            .phoneNumber(phoneNumber);

        updatedRating
            .person(person);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getPerson().getId()).isEqualTo(ratingDB.getPerson().getId());
        assertThat(testRating.getPerson().getCompanyId()).isEqualTo(ratingDB.getPerson().getCompanyId());
        assertThat(testRating.getPerson().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(testRating.getPerson().getFirstName()).isEqualTo("Jan");
        assertThat(testRating.getPerson().getLastName()).isEqualTo("Kowalski");

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updatePersonDifferentTransId() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        Integer companyId = updatedRating.getPerson().getCompanyId();

        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());

        String phoneNumber = "660110550";
        Person person = updatedRating.getPerson()
            .companyId(5666)
            .firstName("Jan")
            .lastName("Kowalski")
            .phoneNumber(phoneNumber);

        updatedRating
            .person(person);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getPerson().getId()).isNotEqualTo(ratingDB.getPerson().getId());
        assertThat(testRating.getPerson().getCompanyId()).isNotEqualTo(companyId);
        assertThat(testRating.getPerson().getCompanyId()).isEqualTo(5666);
        assertThat(testRating.getPerson().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(testRating.getPerson().getFirstName()).isEqualTo("Jan");
        assertThat(testRating.getPerson().getLastName()).isEqualTo("Kowalski");

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updatePersonDifferentTransIdNullId() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        Long personId = updatedRating.getPerson().getId();
        Integer companyId = updatedRating.getPerson().getCompanyId();

        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());

        String phoneNumber = "660110550";
        Person person = new Person()
            .companyId(5666)
            .firstName("Jan")
            .lastName("Kowalski")
            .phoneNumber(phoneNumber);
        person.setId(null);
        updatedRating
            .person(person);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getPerson().getId()).isNotEqualTo(personId);
        assertThat(testRating.getPerson().getCompanyId()).isNotEqualTo(companyId);
        assertThat(testRating.getPerson().getCompanyId()).isEqualTo(5666);
        assertThat(testRating.getPerson().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(testRating.getPerson().getFirstName()).isEqualTo("Jan");
        assertThat(testRating.getPerson().getLastName()).isEqualTo("Kowalski");

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updatePersonNullIdStaysUnchanged() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        Long personId = updatedRating.getPerson().getId();

        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());

        Person person = new Person()
            .firstName(updatedRating.getPerson().getFirstName())
            .lastName(updatedRating.getPerson().getLastName())
            .phoneNumber(updatedRating.getPerson().getPhoneNumber())
            .companyId(updatedRating.getPerson().getCompanyId());

        updatedRating
            .person(person);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getPerson().getId()).isEqualTo(personId);
        assertThat(testRating.getPerson().getCompanyId()).isEqualTo(ratingDB.getPerson().getCompanyId());
        assertThat(testRating.getPerson().getPhoneNumber()).isEqualTo(ratingDB.getPerson().getPhoneNumber());
        assertThat(testRating.getPerson().getFirstName()).isEqualTo(ratingDB.getPerson().getFirstName());
        assertThat(testRating.getPerson().getLastName()).isEqualTo(ratingDB.getPerson().getLastName());

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updateCarrier() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();

        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());

        Carrier carrier = updatedRating.getCarrier()
            .nip("NIP")
            .acronym("NN")
            .name("New name");

        updatedRating
            .carrier(carrier);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getCarrier().getId()).isEqualTo(ratingDB.getCarrier().getId());
        assertThat(testRating.getCarrier().getAcronym()).isEqualTo("NN");
        assertThat(testRating.getCarrier().getNip()).isEqualTo("NIP");
        assertThat(testRating.getCarrier().getName()).isEqualTo("New name");

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updateCarrierDifferentTransId() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();

        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());

        Carrier carrier = updatedRating.getCarrier()
            .transId(56433)
            .nip("NIP")
            .acronym("NN")
            .name("New name");

        updatedRating
            .carrier(carrier);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getCarrier().getId()).isNotEqualTo(ratingDB.getCarrier().getId());
        assertThat(testRating.getCarrier().getTransId()).isNotEqualTo(ratingDB.getCarrier().getId());
        assertThat(testRating.getCarrier().getTransId()).isEqualTo(56433);
        assertThat(testRating.getCarrier().getAcronym()).isEqualTo("NN");
        assertThat(testRating.getCarrier().getNip()).isEqualTo("NIP");
        assertThat(testRating.getCarrier().getName()).isEqualTo("New name");

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updateCarrierDifferentTransIdNullId() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();

        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());

        Carrier carrier = updatedRating.getCarrier()
            .transId(56433)
            .nip("NIP")
            .acronym("NN")
            .name("New name");
        carrier.setId(null);

        updatedRating
            .carrier(carrier);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getCarrier().getId()).isNotEqualTo(ratingDB.getCarrier().getId());
        assertThat(testRating.getCarrier().getTransId()).isNotEqualTo(ratingDB.getCarrier().getId());
        assertThat(testRating.getCarrier().getTransId()).isEqualTo(56433);
        assertThat(testRating.getCarrier().getAcronym()).isEqualTo("NN");
        assertThat(testRating.getCarrier().getNip()).isEqualTo("NIP");
        assertThat(testRating.getCarrier().getName()).isEqualTo("New name");

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void updateCarrierNullIdStaysUnchanged() throws Exception {
        // Initialize the database
        Rating ratingDB = mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        Long carrierId = updatedRating.getCarrier().getId();

        em.detach(updatedRating);
        em.detach(updatedRating.getCarrier());
        em.detach(updatedRating.getPerson());

        Carrier carrier = new Carrier()
            .name(updatedRating.getCarrier().getName())
            .transId(updatedRating.getCarrier().getTransId())
            .acronym(updatedRating.getCarrier().getAcronym())
            .nip(updatedRating.getCarrier().getNip());

        updatedRating
            .carrier(carrier);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getCarrier().getId()).isEqualTo(carrierId);
        assertThat(testRating.getCarrier().getTransId()).isEqualTo(ratingDB.getCarrier().getTransId());
        assertThat(testRating.getCarrier().getAcronym()).isEqualTo(ratingDB.getCarrier().getAcronym());
        assertThat(testRating.getCarrier().getNip()).isEqualTo(ratingDB.getCarrier().getNip());
        assertThat(testRating.getCarrier().getName()).isEqualTo(ratingDB.getCarrier().getName());

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void addAltCarrierAndPerson() throws Exception {
        // Initialize the database
        mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        em.detach(updatedRating);

        Person altPerson = new Person()
            .companyId(12)
            .firstName("Marian")
            .lastName("Kowalski")
            .phoneNumber("123321123");

        Carrier altCarrier = new Carrier()
            .transId(12345)
            .name("Marian Transport sp. z o.o.");


        updatedRating
            .addAlternative(true)
            .altPerson(altPerson)
            .altCarrier(altCarrier);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.getAltPerson()).isNotNull();
        assertThat(testRating.getAltPerson().getCompanyId()).isEqualTo(12);
        assertThat(testRating.getAltPerson().getFirstName()).isEqualTo("Marian");
        assertThat(testRating.getAltPerson().getLastName()).isEqualTo("Kowalski");
        assertThat(testRating.getAltPerson().getPhoneNumber()).isEqualTo("123321123");
        assertThat(testRating.getAltPerson().getCarrier()).isEqualTo(testRating.getAltCarrier());
        assertThat(testRating.getAltCarrier()).isNotNull();
        assertThat(testRating.getAltCarrier().getTransId()).isEqualTo(12345);
        assertThat(testRating.getAltCarrier().getName()).isEqualTo("Marian Transport sp. z o.o.");

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }


    @Test
    @Transactional
    @WithUserDetails
    public void addAltCarrierAndPersonFailFalseAddAlternative() throws Exception {
        // Initialize the database
        mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        em.detach(updatedRating);

        Person altPerson = new Person()
            .companyId(12)
            .firstName("Marian")
            .lastName("Kowalski")
            .phoneNumber("123321123");

        Carrier altCarrier = new Carrier()
            .transId(12345)
            .name("Marian Transport sp. z o.o.");


        updatedRating
            .addAlternative(false)
            .altPerson(altPerson)
            .altCarrier(altCarrier);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isOk());

        // Validate the Rating in the database
        List<Rating> ratingList = ratingRepository.findAll();
        assertThat(ratingList).hasSize(databaseSizeBeforeUpdate);
        Rating testRating = ratingList.get(ratingList.size() - 1);
        assertThat(testRating.isAddAlternative()).isFalse();
        assertThat(testRating.getAltPerson()).isNull();
        assertThat(testRating.getAltCarrier()).isNull();

        // Validate the Rating in Elasticsearch
        verify(mockRatingSearchRepository, times(1)).save(testRating);
    }

    @Test
    @Transactional
    @WithUserDetails
    public void addAltCarrierFailMissingAltPerson() throws Exception {
        // Initialize the database
        mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        em.detach(updatedRating);

        Carrier altCarrier = new Carrier()
            .transId(12345)
            .name("Marian Transport sp. z o.o.");


        updatedRating
            .addAlternative(true)
            .altCarrier(altCarrier);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Person cannot be null"));
    }

    @Test
    @Transactional
    @WithUserDetails
    public void addAltPersonFailMissingAltCarrier() throws Exception {
        // Initialize the database
        mockRatingExtService.save(rating);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockRatingSearchRepository);

        int databaseSizeBeforeUpdate = ratingRepository.findAll().size();

        // Update the rating
        Rating updatedRating = ratingRepository.findById(rating.getId()).get();
        em.detach(updatedRating);

        Person altPerson = new Person()
            .companyId(12)
            .firstName("Marian")
            .lastName("Kowalski")
            .phoneNumber("123321123");


        updatedRating
            .addAlternative(true)
            .altPerson(altPerson);

        restRatingMockMvc.perform(put("/api/ext/ratings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRating)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.title").value("Carrier cannot be null"));
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
