package com.github.netstart.zssn.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.netstart.zssn.service.ItemService;
import com.github.netstart.zssn.web.rest.errors.BadRequestAlertException;
import com.github.netstart.zssn.web.rest.util.HeaderUtil;
import com.github.netstart.zssn.web.rest.util.PaginationUtil;
import com.github.netstart.zssn.service.dto.ItemDTO;
import com.github.netstart.zssn.service.dto.ItemCriteria;
import com.github.netstart.zssn.service.ItemQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Item.
 */
@RestController
@RequestMapping("/api")
public class ItemResource {

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);

    private static final String ENTITY_NAME = "item";

    private final ItemService itemService;

    private final ItemQueryService itemQueryService;

    public ItemResource(ItemService itemService, ItemQueryService itemQueryService) {
        this.itemService = itemService;
        this.itemQueryService = itemQueryService;
    }

    /**
     * POST  /items : Create a new item.
     *
     * @param itemDTO the itemDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itemDTO, or with status 400 (Bad Request) if the item has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/items")
    @Timed
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) throws URISyntaxException {
        log.debug("REST request to save Item : {}", itemDTO);
        if (itemDTO.getId() != null) {
            throw new BadRequestAlertException("A new item cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemDTO result = itemService.save(itemDTO);
        return ResponseEntity.created(new URI("/api/items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /items : Updates an existing item.
     *
     * @param itemDTO the itemDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemDTO,
     * or with status 400 (Bad Request) if the itemDTO is not valid,
     * or with status 500 (Internal Server Error) if the itemDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/items")
    @Timed
    public ResponseEntity<ItemDTO> updateItem(@Valid @RequestBody ItemDTO itemDTO) throws URISyntaxException {
        log.debug("REST request to update Item : {}", itemDTO);
        if (itemDTO.getId() == null) {
            return createItem(itemDTO);
        }
        ItemDTO result = itemService.save(itemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /items : get all the items.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of items in body
     */
    @GetMapping("/items")
    @Timed
    public ResponseEntity<List<ItemDTO>> getAllItems(ItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Items by criteria: {}", criteria);
        Page<ItemDTO> page = itemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /items/:id : get the "id" item.
     *
     * @param id the id of the itemDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemDTO, or with status 404 (Not Found)
     */
    @GetMapping("/items/{id}")
    @Timed
    public ResponseEntity<ItemDTO> getItem(@PathVariable Long id) {
        log.debug("REST request to get Item : {}", id);
        ItemDTO itemDTO = itemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itemDTO));
    }

    /**
     * DELETE  /items/:id : delete the "id" item.
     *
     * @param id the id of the itemDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/items/{id}")
    @Timed
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.debug("REST request to delete Item : {}", id);
        itemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
