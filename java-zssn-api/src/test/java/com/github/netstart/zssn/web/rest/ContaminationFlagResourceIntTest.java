package com.github.netstart.zssn.web.rest;

import com.github.netstart.zssn.ZssnApp;

import com.github.netstart.zssn.domain.ContaminationFlag;
import com.github.netstart.zssn.domain.Survivor;
import com.github.netstart.zssn.domain.Survivor;
import com.github.netstart.zssn.repository.ContaminationFlagRepository;
import com.github.netstart.zssn.service.ContaminationFlagService;
import com.github.netstart.zssn.service.dto.ContaminationFlagDTO;
import com.github.netstart.zssn.service.mapper.ContaminationFlagMapper;
import com.github.netstart.zssn.web.rest.errors.ExceptionTranslator;
import com.github.netstart.zssn.service.dto.ContaminationFlagCriteria;
import com.github.netstart.zssn.service.ContaminationFlagQueryService;

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
 * Test class for the ContaminationFlagResource REST controller.
 *
 * @see ContaminationFlagResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZssnApp.class)
public class ContaminationFlagResourceIntTest {

    @Autowired
    private ContaminationFlagRepository contaminationFlagRepository;

    @Autowired
    private ContaminationFlagMapper contaminationFlagMapper;

    @Autowired
    private ContaminationFlagService contaminationFlagService;

    @Autowired
    private ContaminationFlagQueryService contaminationFlagQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContaminationFlagMockMvc;

    private ContaminationFlag contaminationFlag;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContaminationFlagResource contaminationFlagResource = new ContaminationFlagResource(contaminationFlagService, contaminationFlagQueryService);
        this.restContaminationFlagMockMvc = MockMvcBuilders.standaloneSetup(contaminationFlagResource)
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
    public static ContaminationFlag createEntity(EntityManager em) {
        ContaminationFlag contaminationFlag = new ContaminationFlag();
        // Add required entity
        Survivor reportedBy = SurvivorResourceIntTest.createEntity(em);
        em.persist(reportedBy);
        em.flush();
        contaminationFlag.setReportedBy(reportedBy);
        // Add required entity
        Survivor reported = SurvivorResourceIntTest.createEntity(em);
        em.persist(reported);
        em.flush();
        contaminationFlag.setReported(reported);
        return contaminationFlag;
    }

    @Before
    public void initTest() {
        contaminationFlag = createEntity(em);
    }

    @Test
    @Transactional
    public void createContaminationFlag() throws Exception {
        int databaseSizeBeforeCreate = contaminationFlagRepository.findAll().size();

        // Create the ContaminationFlag
        ContaminationFlagDTO contaminationFlagDTO = contaminationFlagMapper.toDto(contaminationFlag);
        restContaminationFlagMockMvc.perform(post("/api/contamination-flags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaminationFlagDTO)))
            .andExpect(status().isCreated());

        // Validate the ContaminationFlag in the database
        List<ContaminationFlag> contaminationFlagList = contaminationFlagRepository.findAll();
        assertThat(contaminationFlagList).hasSize(databaseSizeBeforeCreate + 1);
        ContaminationFlag testContaminationFlag = contaminationFlagList.get(contaminationFlagList.size() - 1);
    }

    @Test
    @Transactional
    public void createContaminationFlagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contaminationFlagRepository.findAll().size();

        // Create the ContaminationFlag with an existing ID
        contaminationFlag.setId(1L);
        ContaminationFlagDTO contaminationFlagDTO = contaminationFlagMapper.toDto(contaminationFlag);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaminationFlagMockMvc.perform(post("/api/contamination-flags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaminationFlagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContaminationFlag in the database
        List<ContaminationFlag> contaminationFlagList = contaminationFlagRepository.findAll();
        assertThat(contaminationFlagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContaminationFlags() throws Exception {
        // Initialize the database
        contaminationFlagRepository.saveAndFlush(contaminationFlag);

        // Get all the contaminationFlagList
        restContaminationFlagMockMvc.perform(get("/api/contamination-flags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaminationFlag.getId().intValue())));
    }

    @Test
    @Transactional
    public void getContaminationFlag() throws Exception {
        // Initialize the database
        contaminationFlagRepository.saveAndFlush(contaminationFlag);

        // Get the contaminationFlag
        restContaminationFlagMockMvc.perform(get("/api/contamination-flags/{id}", contaminationFlag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contaminationFlag.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllContaminationFlagsByReportedByIsEqualToSomething() throws Exception {
        // Initialize the database
        Survivor reportedBy = SurvivorResourceIntTest.createEntity(em);
        em.persist(reportedBy);
        em.flush();
        contaminationFlag.setReportedBy(reportedBy);
        contaminationFlagRepository.saveAndFlush(contaminationFlag);
        Long reportedById = reportedBy.getId();

        // Get all the contaminationFlagList where reportedBy equals to reportedById
        defaultContaminationFlagShouldBeFound("reportedById.equals=" + reportedById);

        // Get all the contaminationFlagList where reportedBy equals to reportedById + 1
        defaultContaminationFlagShouldNotBeFound("reportedById.equals=" + (reportedById + 1));
    }


    @Test
    @Transactional
    public void getAllContaminationFlagsByReportedIsEqualToSomething() throws Exception {
        // Initialize the database
        Survivor reported = SurvivorResourceIntTest.createEntity(em);
        em.persist(reported);
        em.flush();
        contaminationFlag.setReported(reported);
        contaminationFlagRepository.saveAndFlush(contaminationFlag);
        Long reportedId = reported.getId();

        // Get all the contaminationFlagList where reported equals to reportedId
        defaultContaminationFlagShouldBeFound("reportedId.equals=" + reportedId);

        // Get all the contaminationFlagList where reported equals to reportedId + 1
        defaultContaminationFlagShouldNotBeFound("reportedId.equals=" + (reportedId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContaminationFlagShouldBeFound(String filter) throws Exception {
        restContaminationFlagMockMvc.perform(get("/api/contamination-flags?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaminationFlag.getId().intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContaminationFlagShouldNotBeFound(String filter) throws Exception {
        restContaminationFlagMockMvc.perform(get("/api/contamination-flags?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingContaminationFlag() throws Exception {
        // Get the contaminationFlag
        restContaminationFlagMockMvc.perform(get("/api/contamination-flags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContaminationFlag() throws Exception {
        // Initialize the database
        contaminationFlagRepository.saveAndFlush(contaminationFlag);
        int databaseSizeBeforeUpdate = contaminationFlagRepository.findAll().size();

        // Update the contaminationFlag
        ContaminationFlag updatedContaminationFlag = contaminationFlagRepository.findOne(contaminationFlag.getId());
        // Disconnect from session so that the updates on updatedContaminationFlag are not directly saved in db
        em.detach(updatedContaminationFlag);
        ContaminationFlagDTO contaminationFlagDTO = contaminationFlagMapper.toDto(updatedContaminationFlag);

        restContaminationFlagMockMvc.perform(put("/api/contamination-flags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaminationFlagDTO)))
            .andExpect(status().isOk());

        // Validate the ContaminationFlag in the database
        List<ContaminationFlag> contaminationFlagList = contaminationFlagRepository.findAll();
        assertThat(contaminationFlagList).hasSize(databaseSizeBeforeUpdate);
        ContaminationFlag testContaminationFlag = contaminationFlagList.get(contaminationFlagList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingContaminationFlag() throws Exception {
        int databaseSizeBeforeUpdate = contaminationFlagRepository.findAll().size();

        // Create the ContaminationFlag
        ContaminationFlagDTO contaminationFlagDTO = contaminationFlagMapper.toDto(contaminationFlag);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContaminationFlagMockMvc.perform(put("/api/contamination-flags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contaminationFlagDTO)))
            .andExpect(status().isCreated());

        // Validate the ContaminationFlag in the database
        List<ContaminationFlag> contaminationFlagList = contaminationFlagRepository.findAll();
        assertThat(contaminationFlagList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContaminationFlag() throws Exception {
        // Initialize the database
        contaminationFlagRepository.saveAndFlush(contaminationFlag);
        int databaseSizeBeforeDelete = contaminationFlagRepository.findAll().size();

        // Get the contaminationFlag
        restContaminationFlagMockMvc.perform(delete("/api/contamination-flags/{id}", contaminationFlag.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ContaminationFlag> contaminationFlagList = contaminationFlagRepository.findAll();
        assertThat(contaminationFlagList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaminationFlag.class);
        ContaminationFlag contaminationFlag1 = new ContaminationFlag();
        contaminationFlag1.setId(1L);
        ContaminationFlag contaminationFlag2 = new ContaminationFlag();
        contaminationFlag2.setId(contaminationFlag1.getId());
        assertThat(contaminationFlag1).isEqualTo(contaminationFlag2);
        contaminationFlag2.setId(2L);
        assertThat(contaminationFlag1).isNotEqualTo(contaminationFlag2);
        contaminationFlag1.setId(null);
        assertThat(contaminationFlag1).isNotEqualTo(contaminationFlag2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContaminationFlagDTO.class);
        ContaminationFlagDTO contaminationFlagDTO1 = new ContaminationFlagDTO();
        contaminationFlagDTO1.setId(1L);
        ContaminationFlagDTO contaminationFlagDTO2 = new ContaminationFlagDTO();
        assertThat(contaminationFlagDTO1).isNotEqualTo(contaminationFlagDTO2);
        contaminationFlagDTO2.setId(contaminationFlagDTO1.getId());
        assertThat(contaminationFlagDTO1).isEqualTo(contaminationFlagDTO2);
        contaminationFlagDTO2.setId(2L);
        assertThat(contaminationFlagDTO1).isNotEqualTo(contaminationFlagDTO2);
        contaminationFlagDTO1.setId(null);
        assertThat(contaminationFlagDTO1).isNotEqualTo(contaminationFlagDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contaminationFlagMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contaminationFlagMapper.fromId(null)).isNull();
    }
}
