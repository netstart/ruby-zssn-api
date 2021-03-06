package com.github.netstart.zssn.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.netstart.zssn.service.InventoryService;
import com.github.netstart.zssn.web.rest.errors.BadRequestAlertException;
import com.github.netstart.zssn.web.rest.util.HeaderUtil;
import com.github.netstart.zssn.web.rest.util.PaginationUtil;
import com.github.netstart.zssn.service.dto.InventoryDTO;
import com.github.netstart.zssn.service.dto.InventoryCriteria;
import com.github.netstart.zssn.service.InventoryQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Inventory.
 */
@RestController
@RequestMapping("/api")
public class InventoryResource {

    private final Logger log = LoggerFactory.getLogger(InventoryResource.class);

    private static final String ENTITY_NAME = "inventory";

    private final InventoryService inventoryService;

    private final InventoryQueryService inventoryQueryService;

    public InventoryResource(InventoryService inventoryService, InventoryQueryService inventoryQueryService) {
        this.inventoryService = inventoryService;
        this.inventoryQueryService = inventoryQueryService;
    }

    /**
     * POST  /inventories : Create a new inventory.
     *
     * @param inventoryDTO the inventoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inventoryDTO, or with status 400 (Bad Request) if the inventory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/inventories")
    @Timed
    public ResponseEntity<InventoryDTO> createInventory(@RequestBody InventoryDTO inventoryDTO) throws URISyntaxException {
        log.debug("REST request to save Inventory : {}", inventoryDTO);
        if (inventoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new inventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InventoryDTO result = inventoryService.save(inventoryDTO);
        return ResponseEntity.created(new URI("/api/inventories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inventories : Updates an existing inventory.
     *
     * @param inventoryDTO the inventoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inventoryDTO,
     * or with status 400 (Bad Request) if the inventoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the inventoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/inventories")
    @Timed
    public ResponseEntity<InventoryDTO> updateInventory(@RequestBody InventoryDTO inventoryDTO) throws URISyntaxException {
        log.debug("REST request to update Inventory : {}", inventoryDTO);
        if (inventoryDTO.getId() == null) {
            return createInventory(inventoryDTO);
        }
        InventoryDTO result = inventoryService.save(inventoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inventoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inventories : get all the inventories.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of inventories in body
     */
    @GetMapping("/inventories")
    @Timed
    public ResponseEntity<List<InventoryDTO>> getAllInventories(InventoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Inventories by criteria: {}", criteria);
        Page<InventoryDTO> page = inventoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/inventories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /inventories/:id : get the "id" inventory.
     *
     * @param id the id of the inventoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inventoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/inventories/{id}")
    @Timed
    public ResponseEntity<InventoryDTO> getInventory(@PathVariable Long id) {
        log.debug("REST request to get Inventory : {}", id);
        InventoryDTO inventoryDTO = inventoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(inventoryDTO));
    }

    /**
     * DELETE  /inventories/:id : delete the "id" inventory.
     *
     * @param id the id of the inventoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/inventories/{id}")
    @Timed
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        log.debug("REST request to delete Inventory : {}", id);
        inventoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
