package io.pogorzelski.nitro.carriers.web.rest;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;

import io.pogorzelski.nitro.carriers.domain.Carrier;
import io.pogorzelski.nitro.carriers.repository.CarrierRepository;
import io.pogorzelski.nitro.carriers.service.CarrierService;
import io.pogorzelski.nitro.carriers.service.RatingExtService;
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
 * Test class for the CarrierResource REST controller.
 *
 * @see CarrierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NitroCarriersApp.class)
public class CarrierResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TRANS_ID = 1;
    private static final Integer UPDATED_TRANS_ID = 2;

    private static final String DEFAULT_ACRONYM = "AAAAAAAAAA";
    private static final String UPDATED_ACRONYM = "BBBBBBBBBB";

    private static final String DEFAULT_NIP = "AAAAAAAAAA";
    private static final String UPDATED_NIP = "BBBBBBBBBB";

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private CarrierService carrierService;

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

    private MockMvc restCarrierMockMvc;

    private Carrier carrier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarrierResource carrierResource = new CarrierResource(carrierService, ratingExtService);
        this.restCarrierMockMvc = MockMvcBuilders.standaloneSetup(carrierResource)
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
    public static Carrier createEntity(EntityManager em) {
        Carrier carrier = new Carrier()
            .name(DEFAULT_NAME)
            .transId(DEFAULT_TRANS_ID)
            .acronym(DEFAULT_ACRONYM)
            .nip(DEFAULT_NIP);
        return carrier;
    }

    @Before
    public void initTest() {
        carrier = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarrier() throws Exception {
        int databaseSizeBeforeCreate = carrierRepository.findAll().size();

        // Create the Carrier
        restCarrierMockMvc.perform(post("/api/carriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carrier)))
            .andExpect(status().isCreated());

        // Validate the Carrier in the database
        List<Carrier> carrierList = carrierRepository.findAll();
        assertThat(carrierList).hasSize(databaseSizeBeforeCreate + 1);
        Carrier testCarrier = carrierList.get(carrierList.size() - 1);
        assertThat(testCarrier.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCarrier.getTransId()).isEqualTo(DEFAULT_TRANS_ID);
        assertThat(testCarrier.getAcronym()).isEqualTo(DEFAULT_ACRONYM);
        assertThat(testCarrier.getNip()).isEqualTo(DEFAULT_NIP);
    }

    @Test
    @Transactional
    public void createCarrierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carrierRepository.findAll().size();

        // Create the Carrier with an existing ID
        carrier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarrierMockMvc.perform(post("/api/carriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carrier)))
            .andExpect(status().isBadRequest());

        // Validate the Carrier in the database
        List<Carrier> carrierList = carrierRepository.findAll();
        assertThat(carrierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = carrierRepository.findAll().size();
        // set the field null
        carrier.setName(null);

        // Create the Carrier, which fails.

        restCarrierMockMvc.perform(post("/api/carriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carrier)))
            .andExpect(status().isBadRequest());

        List<Carrier> carrierList = carrierRepository.findAll();
        assertThat(carrierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCarriers() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);

        // Get all the carrierList
        restCarrierMockMvc.perform(get("/api/carriers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].transId").value(hasItem(DEFAULT_TRANS_ID)))
            .andExpect(jsonPath("$.[*].acronym").value(hasItem(DEFAULT_ACRONYM.toString())))
            .andExpect(jsonPath("$.[*].nip").value(hasItem(DEFAULT_NIP.toString())));
    }

    @Test
    @Transactional
    public void getCarrier() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);

        // Get the carrier
        restCarrierMockMvc.perform(get("/api/carriers/{id}", carrier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carrier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.transId").value(DEFAULT_TRANS_ID))
            .andExpect(jsonPath("$.acronym").value(DEFAULT_ACRONYM.toString()))
            .andExpect(jsonPath("$.nip").value(DEFAULT_NIP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarrier() throws Exception {
        // Get the carrier
        restCarrierMockMvc.perform(get("/api/carriers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarrier() throws Exception {
        // Initialize the database
        carrierService.save(carrier);

        int databaseSizeBeforeUpdate = carrierRepository.findAll().size();

        // Update the carrier
        Carrier updatedCarrier = carrierRepository.findById(carrier.getId()).get();
        // Disconnect from session so that the updates on updatedCarrier are not directly saved in db
        em.detach(updatedCarrier);
        updatedCarrier
            .name(UPDATED_NAME)
            .transId(UPDATED_TRANS_ID)
            .acronym(UPDATED_ACRONYM)
            .nip(UPDATED_NIP);

        restCarrierMockMvc.perform(put("/api/carriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCarrier)))
            .andExpect(status().isOk());

        // Validate the Carrier in the database
        List<Carrier> carrierList = carrierRepository.findAll();
        assertThat(carrierList).hasSize(databaseSizeBeforeUpdate);
        Carrier testCarrier = carrierList.get(carrierList.size() - 1);
        assertThat(testCarrier.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCarrier.getTransId()).isEqualTo(UPDATED_TRANS_ID);
        assertThat(testCarrier.getAcronym()).isEqualTo(UPDATED_ACRONYM);
        assertThat(testCarrier.getNip()).isEqualTo(UPDATED_NIP);
    }

    @Test
    @Transactional
    public void updateNonExistingCarrier() throws Exception {
        int databaseSizeBeforeUpdate = carrierRepository.findAll().size();

        // Create the Carrier

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarrierMockMvc.perform(put("/api/carriers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(carrier)))
            .andExpect(status().isBadRequest());

        // Validate the Carrier in the database
        List<Carrier> carrierList = carrierRepository.findAll();
        assertThat(carrierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCarrier() throws Exception {
        // Initialize the database
        carrierService.save(carrier);

        int databaseSizeBeforeDelete = carrierRepository.findAll().size();

        // Delete the carrier
        restCarrierMockMvc.perform(delete("/api/carriers/{id}", carrier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Carrier> carrierList = carrierRepository.findAll();
        assertThat(carrierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carrier.class);
        Carrier carrier1 = new Carrier();
        carrier1.setId(1L);
        Carrier carrier2 = new Carrier();
        carrier2.setId(carrier1.getId());
        assertThat(carrier1).isEqualTo(carrier2);
        carrier2.setId(2L);
        assertThat(carrier1).isNotEqualTo(carrier2);
        carrier1.setId(null);
        assertThat(carrier1).isNotEqualTo(carrier2);
    }
}
