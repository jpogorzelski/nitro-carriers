package io.pogorzelski.nitro.carriers.web.rest;

import io.pogorzelski.nitro.carriers.NitroCarriersApp;

import io.pogorzelski.nitro.carriers.domain.CargoType;
import io.pogorzelski.nitro.carriers.repository.CargoTypeRepository;
import io.pogorzelski.nitro.carriers.service.CargoTypeService;
import io.pogorzelski.nitro.carriers.service.dto.CargoTypeDTO;
import io.pogorzelski.nitro.carriers.service.mapper.CargoTypeMapper;
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

import javax.persistence.EntityManager;
import java.util.List;


import static io.pogorzelski.nitro.carriers.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
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
    private CargoTypeMapper cargoTypeMapper;

    @Autowired
    private CargoTypeService cargoTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

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
            .setMessageConverters(jacksonMessageConverter).build();
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
        CargoTypeDTO cargoTypeDTO = cargoTypeMapper.toDto(cargoType);
        restCargoTypeMockMvc.perform(post("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the CargoType in the database
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CargoType testCargoType = cargoTypeList.get(cargoTypeList.size() - 1);
        assertThat(testCargoType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCargoTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cargoTypeRepository.findAll().size();

        // Create the CargoType with an existing ID
        cargoType.setId(1L);
        CargoTypeDTO cargoTypeDTO = cargoTypeMapper.toDto(cargoType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoTypeMockMvc.perform(post("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CargoType in the database
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cargoTypeRepository.findAll().size();
        // set the field null
        cargoType.setName(null);

        // Create the CargoType, which fails.
        CargoTypeDTO cargoTypeDTO = cargoTypeMapper.toDto(cargoType);

        restCargoTypeMockMvc.perform(post("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoTypeDTO)))
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
        cargoTypeRepository.saveAndFlush(cargoType);

        int databaseSizeBeforeUpdate = cargoTypeRepository.findAll().size();

        // Update the cargoType
        CargoType updatedCargoType = cargoTypeRepository.findById(cargoType.getId()).get();
        // Disconnect from session so that the updates on updatedCargoType are not directly saved in db
        em.detach(updatedCargoType);
        updatedCargoType
            .name(UPDATED_NAME);
        CargoTypeDTO cargoTypeDTO = cargoTypeMapper.toDto(updatedCargoType);

        restCargoTypeMockMvc.perform(put("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoTypeDTO)))
            .andExpect(status().isOk());

        // Validate the CargoType in the database
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeUpdate);
        CargoType testCargoType = cargoTypeList.get(cargoTypeList.size() - 1);
        assertThat(testCargoType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCargoType() throws Exception {
        int databaseSizeBeforeUpdate = cargoTypeRepository.findAll().size();

        // Create the CargoType
        CargoTypeDTO cargoTypeDTO = cargoTypeMapper.toDto(cargoType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoTypeMockMvc.perform(put("/api/cargo-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cargoTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CargoType in the database
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCargoType() throws Exception {
        // Initialize the database
        cargoTypeRepository.saveAndFlush(cargoType);

        int databaseSizeBeforeDelete = cargoTypeRepository.findAll().size();

        // Get the cargoType
        restCargoTypeMockMvc.perform(delete("/api/cargo-types/{id}", cargoType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CargoType> cargoTypeList = cargoTypeRepository.findAll();
        assertThat(cargoTypeList).hasSize(databaseSizeBeforeDelete - 1);
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

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoTypeDTO.class);
        CargoTypeDTO cargoTypeDTO1 = new CargoTypeDTO();
        cargoTypeDTO1.setId(1L);
        CargoTypeDTO cargoTypeDTO2 = new CargoTypeDTO();
        assertThat(cargoTypeDTO1).isNotEqualTo(cargoTypeDTO2);
        cargoTypeDTO2.setId(cargoTypeDTO1.getId());
        assertThat(cargoTypeDTO1).isEqualTo(cargoTypeDTO2);
        cargoTypeDTO2.setId(2L);
        assertThat(cargoTypeDTO1).isNotEqualTo(cargoTypeDTO2);
        cargoTypeDTO1.setId(null);
        assertThat(cargoTypeDTO1).isNotEqualTo(cargoTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cargoTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cargoTypeMapper.fromId(null)).isNull();
    }
}
