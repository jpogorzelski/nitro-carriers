package io.pogorzelski.nitro.carriers.web.rest;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;

import io.pogorzelski.nitro.carriers.domain.Rating;
import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.domain.Person;
import io.pogorzelski.nitro.carriers.domain.Country;
import io.pogorzelski.nitro.carriers.repository.RatingRepository;
import io.pogorzelski.nitro.carriers.repository.search.RatingSearchRepository;
import io.pogorzelski.nitro.carriers.service.RatingService;
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

import io.pogorzelski.nitro.carriers.domain.enumeration.CargoType;
import io.pogorzelski.nitro.carriers.domain.enumeration.Grade;
/**
 * Test class for the RatingResource REST controller.
 *
 * @see RatingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NitroCarriersApp.class)
public class RatingResourceIntTest {

    private static final String DEFAULT_CHARGE_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CHARGE_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DISCHARGE_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DISCHARGE_POSTAL_CODE = "BBBBBBBBBB";

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

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingService ratingService;

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
        final RatingResource ratingResource = new RatingResource(ratingService);
        this.restRatingMockMvc = MockMvcBuilders.standaloneSetup(ratingResource)
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
        Rating rating = new Rating()
            .chargePostalCode(DEFAULT_CHARGE_POSTAL_CODE)
            .dischargePostalCode(DEFAULT_DISCHARGE_POSTAL_CODE)
            .cargoType(DEFAULT_CARGO_TYPE)
            .distance(DEFAULT_DISTANCE)
            .contact(DEFAULT_CONTACT)
            .price(DEFAULT_PRICE)
            .flexibility(DEFAULT_FLEXIBILITY)
            .recommendation(DEFAULT_RECOMMENDATION)
            .average(DEFAULT_AVERAGE)
            .remarks(DEFAULT_REMARKS);
        // Add required entity
        Carrier carrier = CarrierResourceIntTest.createEntity(em);
        em.persist(carrier);
        em.flush();
        rating.setCarrier(carrier);
        // Add required entity
        Person person = PersonResourceIntTest.createEntity(em);
        em.persist(person);
        em.flush();
        rating.setPerson(person);
        // Add required entity
        Country country = CountryResourceIntTest.createEntity(em);
        em.persist(country);
        em.flush();
        rating.setChargeCountry(country);
        // Add required entity
        rating.setDischargeCountry(country);
        return rating;
    }

    @Before
    public void initTest() {
        rating = createEntity(em);
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
            .andExpect(jsonPath("$.[*].cargoType").value(hasItem(DEFAULT_CARGO_TYPE.toString())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].flexibility").value(hasItem(DEFAULT_FLEXIBILITY)))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION.toString())))
            .andExpect(jsonPath("$.[*].average").value(hasItem(DEFAULT_AVERAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS.toString())));
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
            .andExpect(jsonPath("$.cargoType").value(DEFAULT_CARGO_TYPE.toString()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.flexibility").value(DEFAULT_FLEXIBILITY))
            .andExpect(jsonPath("$.recommendation").value(DEFAULT_RECOMMENDATION.toString()))
            .andExpect(jsonPath("$.average").value(DEFAULT_AVERAGE.doubleValue()))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS.toString()));
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
    public void searchRating() throws Exception {
        // Initialize the database
        ratingService.save(rating);
        when(mockRatingSearchRepository.search(queryStringQuery("id:" + rating.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(rating), PageRequest.of(0, 1), 1));
        // Search the rating
        restRatingMockMvc.perform(get("/api/_search/ratings?query=id:" + rating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rating.getId().intValue())))
            .andExpect(jsonPath("$.[*].chargePostalCode").value(hasItem(DEFAULT_CHARGE_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].dischargePostalCode").value(hasItem(DEFAULT_DISCHARGE_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].cargoType").value(hasItem(DEFAULT_CARGO_TYPE.toString())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].flexibility").value(hasItem(DEFAULT_FLEXIBILITY)))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION.toString())))
            .andExpect(jsonPath("$.[*].average").value(hasItem(DEFAULT_AVERAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
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
