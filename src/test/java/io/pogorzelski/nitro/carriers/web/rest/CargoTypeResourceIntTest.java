package io.pogorzelski.nitro.carriers.web.rest;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;

import io.pogorzelski.nitro.carriers.domain.CargoType;
import io.pogorzelski.nitro.carriers.repository.CargoTypeRepository;
import io.pogorzelski.nitro.carriers.repository.search.CargoTypeSearchRepository;
import io.pogorzelski.nitro.carriers.service.CargoTypeService;
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
 * Test class for the CargoTypeResource REST controller.
 *
 * @see CargoTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NitroCarriersApp.class)
public class CargoTypeResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CargoTypeRepository cargoTypeRepository;

    @Autowired
    private CargoTypeService cargoTypeService;

    /**
     * This repository is mocked in the io.pogorzelski.nitro.carriers.repository.search test package.
     *
     * @see io.pogorzelski.nitro.carriers.repository.search.CargoTypeSearchRepositoryMockConfiguration
     */
    @Autowired
    private CargoTypeSearchRepository mockCargoTypeSearchRepository;

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

    private MockMvc restCargoTypeMockMvc;

    private CargoType cargoType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CargoTypeResource cargoTypeResource = new CargoTypeResource(cargoTypeService);
        this.restCargoTypeMockMvc = MockMvcBuilders.standaloneSetup(cargoTypeResource)
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
    public static CargoType createEntity(EntityManager em) {
        CargoType cargoType = new CargoType()
            .name(DEFAULT_NAME);
        return cargoType;
    }

    @Before
    public void initTest() {
        cargoType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCargoType() throws Exception {
        int databaseSizeBeforeCreate = cargoTypeRepository.findAll().size();

        // Create the CargoType
        restCargoTypeMockMvc.perform(post("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoType)))
            .andExpect(status().isCreated());

        // Validate the CargoType in the database
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CargoType testCargoType = cargoTypeList.get(cargoTypeList.size() - 1);
        assertThat(testCargoType.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the CargoType in Elasticsearch
        verify(mockCargoTypeSearchRepository, times(1)).save(testCargoType);
    }

    @Test
    @Transactional
    public void createCargoTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cargoTypeRepository.findAll().size();

        // Create the CargoType with an existing ID
        cargoType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoTypeMockMvc.perform(post("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoType)))
            .andExpect(status().isBadRequest());

        // Validate the CargoType in the database
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeCreate);

        // Validate the CargoType in Elasticsearch
        verify(mockCargoTypeSearchRepository, times(0)).save(cargoType);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cargoTypeRepository.findAll().size();
        // set the field null
        cargoType.setName(null);

        // Create the CargoType, which fails.

        restCargoTypeMockMvc.perform(post("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoType)))
            .andExpect(status().isBadRequest());

        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCargoTypes() throws Exception {
        // Initialize the database
        cargoTypeRepository.saveAndFlush(cargoType);

        // Get all the cargoTypeList
        restCargoTypeMockMvc.perform(get("/api/cargo-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getCargoType() throws Exception {
        // Initialize the database
        cargoTypeRepository.saveAndFlush(cargoType);

        // Get the cargoType
        restCargoTypeMockMvc.perform(get("/api/cargo-types/{id}", cargoType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cargoType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCargoType() throws Exception {
        // Get the cargoType
        restCargoTypeMockMvc.perform(get("/api/cargo-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCargoType() throws Exception {
        // Initialize the database
        cargoTypeService.save(cargoType);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockCargoTypeSearchRepository);

        int databaseSizeBeforeUpdate = cargoTypeRepository.findAll().size();

        // Update the cargoType
        CargoType updatedCargoType = cargoTypeRepository.findById(cargoType.getId()).get();
        // Disconnect from session so that the updates on updatedCargoType are not directly saved in db
        em.detach(updatedCargoType);
        updatedCargoType
            .name(UPDATED_NAME);

        restCargoTypeMockMvc.perform(put("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCargoType)))
            .andExpect(status().isOk());

        // Validate the CargoType in the database
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeUpdate);
        CargoType testCargoType = cargoTypeList.get(cargoTypeList.size() - 1);
        assertThat(testCargoType.getName()).isEqualTo(UPDATED_NAME);

        // Validate the CargoType in Elasticsearch
        verify(mockCargoTypeSearchRepository, times(1)).save(testCargoType);
    }

    @Test
    @Transactional
    public void updateNonExistingCargoType() throws Exception {
        int databaseSizeBeforeUpdate = cargoTypeRepository.findAll().size();

        // Create the CargoType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoTypeMockMvc.perform(put("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoType)))
            .andExpect(status().isBadRequest());

        // Validate the CargoType in the database
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CargoType in Elasticsearch
        verify(mockCargoTypeSearchRepository, times(0)).save(cargoType);
    }

    @Test
    @Transactional
    public void deleteCargoType() throws Exception {
        // Initialize the database
        cargoTypeService.save(cargoType);

        int databaseSizeBeforeDelete = cargoTypeRepository.findAll().size();

        // Delete the cargoType
        restCargoTypeMockMvc.perform(delete("/api/cargo-types/{id}", cargoType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CargoType in Elasticsearch
        verify(mockCargoTypeSearchRepository, times(1)).deleteById(cargoType.getId());
    }

    @Test
    @Transactional
    public void searchCargoType() throws Exception {
        // Initialize the database
        cargoTypeService.save(cargoType);
        when(mockCargoTypeSearchRepository.search(queryStringQuery("id:" + cargoType.getId())))
            .thenReturn(Collections.singletonList(cargoType));
        // Search the cargoType
        restCargoTypeMockMvc.perform(get("/api/_search/cargo-types?query=id:" + cargoType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoType.class);
        CargoType cargoType1 = new CargoType();
        cargoType1.setId(1L);
        CargoType cargoType2 = new CargoType();
        cargoType2.setId(cargoType1.getId());
        assertThat(cargoType1).isEqualTo(cargoType2);
        cargoType2.setId(2L);
        assertThat(cargoType1).isNotEqualTo(cargoType2);
        cargoType1.setId(null);
        assertThat(cargoType1).isNotEqualTo(cargoType2);
    }
}
