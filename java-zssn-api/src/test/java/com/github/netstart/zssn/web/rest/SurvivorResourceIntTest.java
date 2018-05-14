package com.github.netstart.zssn.web.rest;

import com.github.netstart.zssn.ZssnApp;

import com.github.netstart.zssn.domain.Survivor;
import com.github.netstart.zssn.domain.Location;
import com.github.netstart.zssn.domain.Inventory;
import com.github.netstart.zssn.domain.ContaminationFlag;
import com.github.netstart.zssn.repository.SurvivorRepository;
import com.github.netstart.zssn.service.SurvivorService;
import com.github.netstart.zssn.service.dto.SurvivorDTO;
import com.github.netstart.zssn.service.mapper.SurvivorMapper;
import com.github.netstart.zssn.web.rest.errors.ExceptionTranslator;
import com.github.netstart.zssn.service.dto.SurvivorCriteria;
import com.github.netstart.zssn.service.SurvivorQueryService;

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

import static com.github.netstart.zssn.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SurvivorResource REST controller.
 *
 * @see SurvivorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZssnApp.class)
public class SurvivorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    @Autowired
    private SurvivorRepository survivorRepository;

    @Autowired
    private SurvivorMapper survivorMapper;

    @Autowired
    private SurvivorService survivorService;

    @Autowired
    private SurvivorQueryService survivorQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSurvivorMockMvc;

    private Survivor survivor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SurvivorResource survivorResource = new SurvivorResource(survivorService, survivorQueryService);
        this.restSurvivorMockMvc = MockMvcBuilders.standaloneSetup(survivorResource)
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
    public static Survivor createEntity(EntityManager em) {
        Survivor survivor = new Survivor()
            .name(DEFAULT_NAME)
            .age(DEFAULT_AGE);
        // Add required entity
        Location lastLocation = LocationResourceIntTest.createEntity(em);
        em.persist(lastLocation);
        em.flush();
        survivor.setLastLocation(lastLocation);
        // Add required entity
        Inventory inventory = InventoryResourceIntTest.createEntity(em);
        em.persist(inventory);
        em.flush();
        survivor.setInventory(inventory);
        return survivor;
    }

    @Before
    public void initTest() {
        survivor = createEntity(em);
    }

    @Test
    @Transactional
    public void createSurvivor() throws Exception {
        int databaseSizeBeforeCreate = survivorRepository.findAll().size();

        // Create the Survivor
        SurvivorDTO survivorDTO = survivorMapper.toDto(survivor);
        restSurvivorMockMvc.perform(post("/api/survivors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(survivorDTO)))
            .andExpect(status().isCreated());

        // Validate the Survivor in the database
        List<Survivor> survivorList = survivorRepository.findAll();
        assertThat(survivorList).hasSize(databaseSizeBeforeCreate + 1);
        Survivor testSurvivor = survivorList.get(survivorList.size() - 1);
        assertThat(testSurvivor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSurvivor.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    public void createSurvivorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = survivorRepository.findAll().size();

        // Create the Survivor with an existing ID
        survivor.setId(1L);
        SurvivorDTO survivorDTO = survivorMapper.toDto(survivor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurvivorMockMvc.perform(post("/api/survivors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(survivorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Survivor in the database
        List<Survivor> survivorList = survivorRepository.findAll();
        assertThat(survivorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = survivorRepository.findAll().size();
        // set the field null
        survivor.setName(null);

        // Create the Survivor, which fails.
        SurvivorDTO survivorDTO = survivorMapper.toDto(survivor);

        restSurvivorMockMvc.perform(post("/api/survivors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(survivorDTO)))
            .andExpect(status().isBadRequest());

        List<Survivor> survivorList = survivorRepository.findAll();
        assertThat(survivorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = survivorRepository.findAll().size();
        // set the field null
        survivor.setAge(null);

        // Create the Survivor, which fails.
        SurvivorDTO survivorDTO = survivorMapper.toDto(survivor);

        restSurvivorMockMvc.perform(post("/api/survivors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(survivorDTO)))
            .andExpect(status().isBadRequest());

        List<Survivor> survivorList = survivorRepository.findAll();
        assertThat(survivorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSurvivors() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList
        restSurvivorMockMvc.perform(get("/api/survivors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(survivor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    @Test
    @Transactional
    public void getSurvivor() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get the survivor
        restSurvivorMockMvc.perform(get("/api/survivors/{id}", survivor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(survivor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }

    @Test
    @Transactional
    public void getAllSurvivorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList where name equals to DEFAULT_NAME
        defaultSurvivorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the survivorList where name equals to UPDATED_NAME
        defaultSurvivorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSurvivorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSurvivorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the survivorList where name equals to UPDATED_NAME
        defaultSurvivorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllSurvivorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList where name is not null
        defaultSurvivorShouldBeFound("name.specified=true");

        // Get all the survivorList where name is null
        defaultSurvivorShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllSurvivorsByAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList where age equals to DEFAULT_AGE
        defaultSurvivorShouldBeFound("age.equals=" + DEFAULT_AGE);

        // Get all the survivorList where age equals to UPDATED_AGE
        defaultSurvivorShouldNotBeFound("age.equals=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllSurvivorsByAgeIsInShouldWork() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList where age in DEFAULT_AGE or UPDATED_AGE
        defaultSurvivorShouldBeFound("age.in=" + DEFAULT_AGE + "," + UPDATED_AGE);

        // Get all the survivorList where age equals to UPDATED_AGE
        defaultSurvivorShouldNotBeFound("age.in=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllSurvivorsByAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList where age is not null
        defaultSurvivorShouldBeFound("age.specified=true");

        // Get all the survivorList where age is null
        defaultSurvivorShouldNotBeFound("age.specified=false");
    }

    @Test
    @Transactional
    public void getAllSurvivorsByAgeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList where age greater than or equals to DEFAULT_AGE
        defaultSurvivorShouldBeFound("age.greaterOrEqualThan=" + DEFAULT_AGE);

        // Get all the survivorList where age greater than or equals to UPDATED_AGE
        defaultSurvivorShouldNotBeFound("age.greaterOrEqualThan=" + UPDATED_AGE);
    }

    @Test
    @Transactional
    public void getAllSurvivorsByAgeIsLessThanSomething() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);

        // Get all the survivorList where age less than or equals to DEFAULT_AGE
        defaultSurvivorShouldNotBeFound("age.lessThan=" + DEFAULT_AGE);

        // Get all the survivorList where age less than or equals to UPDATED_AGE
        defaultSurvivorShouldBeFound("age.lessThan=" + UPDATED_AGE);
    }


    @Test
    @Transactional
    public void getAllSurvivorsByLastLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location lastLocation = LocationResourceIntTest.createEntity(em);
        em.persist(lastLocation);
        em.flush();
        survivor.setLastLocation(lastLocation);
        survivorRepository.saveAndFlush(survivor);
        Long lastLocationId = lastLocation.getId();

        // Get all the survivorList where lastLocation equals to lastLocationId
        defaultSurvivorShouldBeFound("lastLocationId.equals=" + lastLocationId);

        // Get all the survivorList where lastLocation equals to lastLocationId + 1
        defaultSurvivorShouldNotBeFound("lastLocationId.equals=" + (lastLocationId + 1));
    }


    @Test
    @Transactional
    public void getAllSurvivorsByInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        Inventory inventory = InventoryResourceIntTest.createEntity(em);
        em.persist(inventory);
        em.flush();
        survivor.setInventory(inventory);
        survivorRepository.saveAndFlush(survivor);
        Long inventoryId = inventory.getId();

        // Get all the survivorList where inventory equals to inventoryId
        defaultSurvivorShouldBeFound("inventoryId.equals=" + inventoryId);

        // Get all the survivorList where inventory equals to inventoryId + 1
        defaultSurvivorShouldNotBeFound("inventoryId.equals=" + (inventoryId + 1));
    }


    @Test
    @Transactional
    public void getAllSurvivorsByReportedIsEqualToSomething() throws Exception {
        // Initialize the database
        ContaminationFlag reported = ContaminationFlagResourceIntTest.createEntity(em);
        em.persist(reported);
        em.flush();
        survivor.addReported(reported);
        survivorRepository.saveAndFlush(survivor);
        Long reportedId = reported.getId();

        // Get all the survivorList where reported equals to reportedId
        defaultSurvivorShouldBeFound("reportedId.equals=" + reportedId);

        // Get all the survivorList where reported equals to reportedId + 1
        defaultSurvivorShouldNotBeFound("reportedId.equals=" + (reportedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSurvivorShouldBeFound(String filter) throws Exception {
        restSurvivorMockMvc.perform(get("/api/survivors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(survivor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSurvivorShouldNotBeFound(String filter) throws Exception {
        restSurvivorMockMvc.perform(get("/api/survivors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSurvivor() throws Exception {
        // Get the survivor
        restSurvivorMockMvc.perform(get("/api/survivors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSurvivor() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);
        int databaseSizeBeforeUpdate = survivorRepository.findAll().size();

        // Update the survivor
        Survivor updatedSurvivor = survivorRepository.findOne(survivor.getId());
        // Disconnect from session so that the updates on updatedSurvivor are not directly saved in db
        em.detach(updatedSurvivor);
        updatedSurvivor
            .name(UPDATED_NAME)
            .age(UPDATED_AGE);
        SurvivorDTO survivorDTO = survivorMapper.toDto(updatedSurvivor);

        restSurvivorMockMvc.perform(put("/api/survivors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(survivorDTO)))
            .andExpect(status().isOk());

        // Validate the Survivor in the database
        List<Survivor> survivorList = survivorRepository.findAll();
        assertThat(survivorList).hasSize(databaseSizeBeforeUpdate);
        Survivor testSurvivor = survivorList.get(survivorList.size() - 1);
        assertThat(testSurvivor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSurvivor.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    public void updateNonExistingSurvivor() throws Exception {
        int databaseSizeBeforeUpdate = survivorRepository.findAll().size();

        // Create the Survivor
        SurvivorDTO survivorDTO = survivorMapper.toDto(survivor);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSurvivorMockMvc.perform(put("/api/survivors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(survivorDTO)))
            .andExpect(status().isCreated());

        // Validate the Survivor in the database
        List<Survivor> survivorList = survivorRepository.findAll();
        assertThat(survivorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSurvivor() throws Exception {
        // Initialize the database
        survivorRepository.saveAndFlush(survivor);
        int databaseSizeBeforeDelete = survivorRepository.findAll().size();

        // Get the survivor
        restSurvivorMockMvc.perform(delete("/api/survivors/{id}", survivor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Survivor> survivorList = survivorRepository.findAll();
        assertThat(survivorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Survivor.class);
        Survivor survivor1 = new Survivor();
        survivor1.setId(1L);
        Survivor survivor2 = new Survivor();
        survivor2.setId(survivor1.getId());
        assertThat(survivor1).isEqualTo(survivor2);
        survivor2.setId(2L);
        assertThat(survivor1).isNotEqualTo(survivor2);
        survivor1.setId(null);
        assertThat(survivor1).isNotEqualTo(survivor2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurvivorDTO.class);
        SurvivorDTO survivorDTO1 = new SurvivorDTO();
        survivorDTO1.setId(1L);
        SurvivorDTO survivorDTO2 = new SurvivorDTO();
        assertThat(survivorDTO1).isNotEqualTo(survivorDTO2);
        survivorDTO2.setId(survivorDTO1.getId());
        assertThat(survivorDTO1).isEqualTo(survivorDTO2);
        survivorDTO2.setId(2L);
        assertThat(survivorDTO1).isNotEqualTo(survivorDTO2);
        survivorDTO1.setId(null);
        assertThat(survivorDTO1).isNotEqualTo(survivorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(survivorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(survivorMapper.fromId(null)).isNull();
    }
}
