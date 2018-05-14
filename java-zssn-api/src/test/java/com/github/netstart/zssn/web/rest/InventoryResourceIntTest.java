package com.github.netstart.zssn.web.rest;

import com.github.netstart.zssn.ZssnApp;

import com.github.netstart.zssn.domain.Inventory;
import com.github.netstart.zssn.domain.Item;
import com.github.netstart.zssn.repository.InventoryRepository;
import com.github.netstart.zssn.service.InventoryService;
import com.github.netstart.zssn.service.dto.InventoryDTO;
import com.github.netstart.zssn.service.mapper.InventoryMapper;
import com.github.netstart.zssn.web.rest.errors.ExceptionTranslator;
import com.github.netstart.zssn.service.dto.InventoryCriteria;
import com.github.netstart.zssn.service.InventoryQueryService;

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
 * Test class for the InventoryResource REST controller.
 *
 * @see InventoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZssnApp.class)
public class InventoryResourceIntTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryQueryService inventoryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInventoryMockMvc;

    private Inventory inventory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InventoryResource inventoryResource = new InventoryResource(inventoryService, inventoryQueryService);
        this.restInventoryMockMvc = MockMvcBuilders.standaloneSetup(inventoryResource)
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
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory();
        return inventory;
    }

    @Before
    public void initTest() {
        inventory = createEntity(em);
    }

    @Test
    @Transactional
    public void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);
        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
    }

    @Test
    @Transactional
    public void createInventoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().size();

        // Create the Inventory with an existing ID
        inventory.setId(1L);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryMockMvc.perform(post("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInventories() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get all the inventoryList
        restInventoryMockMvc.perform(get("/api/inventories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())));
    }

    @Test
    @Transactional
    public void getInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);

        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventories/{id}", inventory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(inventory.getId().intValue()));
    }

    @Test
    @Transactional
    public void getAllInventoriesByItensIsEqualToSomething() throws Exception {
        // Initialize the database
        Item itens = ItemResourceIntTest.createEntity(em);
        em.persist(itens);
        em.flush();
        inventory.addItens(itens);
        inventoryRepository.saveAndFlush(inventory);
        Long itensId = itens.getId();

        // Get all the inventoryList where itens equals to itensId
        defaultInventoryShouldBeFound("itensId.equals=" + itensId);

        // Get all the inventoryList where itens equals to itensId + 1
        defaultInventoryShouldNotBeFound("itensId.equals=" + (itensId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultInventoryShouldBeFound(String filter) throws Exception {
        restInventoryMockMvc.perform(get("/api/inventories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventory.getId().intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultInventoryShouldNotBeFound(String filter) throws Exception {
        restInventoryMockMvc.perform(get("/api/inventories?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingInventory() throws Exception {
        // Get the inventory
        restInventoryMockMvc.perform(get("/api/inventories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findOne(inventory.getId());
        // Disconnect from session so that the updates on updatedInventory are not directly saved in db
        em.detach(updatedInventory);
        InventoryDTO inventoryDTO = inventoryMapper.toDto(updatedInventory);

        restInventoryMockMvc.perform(put("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isOk());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().size();

        // Create the Inventory
        InventoryDTO inventoryDTO = inventoryMapper.toDto(inventory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInventoryMockMvc.perform(put("/api/inventories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(inventoryDTO)))
            .andExpect(status().isCreated());

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInventory() throws Exception {
        // Initialize the database
        inventoryRepository.saveAndFlush(inventory);
        int databaseSizeBeforeDelete = inventoryRepository.findAll().size();

        // Get the inventory
        restInventoryMockMvc.perform(delete("/api/inventories/{id}", inventory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Inventory> inventoryList = inventoryRepository.findAll();
        assertThat(inventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventory.class);
        Inventory inventory1 = new Inventory();
        inventory1.setId(1L);
        Inventory inventory2 = new Inventory();
        inventory2.setId(inventory1.getId());
        assertThat(inventory1).isEqualTo(inventory2);
        inventory2.setId(2L);
        assertThat(inventory1).isNotEqualTo(inventory2);
        inventory1.setId(null);
        assertThat(inventory1).isNotEqualTo(inventory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryDTO.class);
        InventoryDTO inventoryDTO1 = new InventoryDTO();
        inventoryDTO1.setId(1L);
        InventoryDTO inventoryDTO2 = new InventoryDTO();
        assertThat(inventoryDTO1).isNotEqualTo(inventoryDTO2);
        inventoryDTO2.setId(inventoryDTO1.getId());
        assertThat(inventoryDTO1).isEqualTo(inventoryDTO2);
        inventoryDTO2.setId(2L);
        assertThat(inventoryDTO1).isNotEqualTo(inventoryDTO2);
        inventoryDTO1.setId(null);
        assertThat(inventoryDTO1).isNotEqualTo(inventoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(inventoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(inventoryMapper.fromId(null)).isNull();
    }
}
