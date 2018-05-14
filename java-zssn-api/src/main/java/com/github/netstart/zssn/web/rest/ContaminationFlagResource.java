package com.github.netstart.zssn.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.netstart.zssn.service.ContaminationFlagService;
import com.github.netstart.zssn.web.rest.errors.BadRequestAlertException;
import com.github.netstart.zssn.web.rest.util.HeaderUtil;
import com.github.netstart.zssn.web.rest.util.PaginationUtil;
import com.github.netstart.zssn.service.dto.ContaminationFlagDTO;
import com.github.netstart.zssn.service.dto.ContaminationFlagCriteria;
import com.github.netstart.zssn.service.ContaminationFlagQueryService;
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
 * REST controller for managing ContaminationFlag.
 */
@RestController
@RequestMapping("/api")
public class ContaminationFlagResource {

    private final Logger log = LoggerFactory.getLogger(ContaminationFlagResource.class);

    private static final String ENTITY_NAME = "contaminationFlag";

    private final ContaminationFlagService contaminationFlagService;

    private final ContaminationFlagQueryService contaminationFlagQueryService;

    public ContaminationFlagResource(ContaminationFlagService contaminationFlagService, ContaminationFlagQueryService contaminationFlagQueryService) {
        this.contaminationFlagService = contaminationFlagService;
        this.contaminationFlagQueryService = contaminationFlagQueryService;
    }

    /**
     * POST  /contamination-flags : Create a new contaminationFlag.
     *
     * @param contaminationFlagDTO the contaminationFlagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contaminationFlagDTO, or with status 400 (Bad Request) if the contaminationFlag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contamination-flags")
    @Timed
    public ResponseEntity<ContaminationFlagDTO> createContaminationFlag(@Valid @RequestBody ContaminationFlagDTO contaminationFlagDTO) throws URISyntaxException {
        log.debug("REST request to save ContaminationFlag : {}", contaminationFlagDTO);
        if (contaminationFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new contaminationFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContaminationFlagDTO result = contaminationFlagService.save(contaminationFlagDTO);
        return ResponseEntity.created(new URI("/api/contamination-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contamination-flags : Updates an existing contaminationFlag.
     *
     * @param contaminationFlagDTO the contaminationFlagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contaminationFlagDTO,
     * or with status 400 (Bad Request) if the contaminationFlagDTO is not valid,
     * or with status 500 (Internal Server Error) if the contaminationFlagDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contamination-flags")
    @Timed
    public ResponseEntity<ContaminationFlagDTO> updateContaminationFlag(@Valid @RequestBody ContaminationFlagDTO contaminationFlagDTO) throws URISyntaxException {
        log.debug("REST request to update ContaminationFlag : {}", contaminationFlagDTO);
        if (contaminationFlagDTO.getId() == null) {
            return createContaminationFlag(contaminationFlagDTO);
        }
        ContaminationFlagDTO result = contaminationFlagService.save(contaminationFlagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contaminationFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contamination-flags : get all the contaminationFlags.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of contaminationFlags in body
     */
    @GetMapping("/contamination-flags")
    @Timed
    public ResponseEntity<List<ContaminationFlagDTO>> getAllContaminationFlags(ContaminationFlagCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContaminationFlags by criteria: {}", criteria);
        Page<ContaminationFlagDTO> page = contaminationFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contamination-flags");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contamination-flags/:id : get the "id" contaminationFlag.
     *
     * @param id the id of the contaminationFlagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contaminationFlagDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contamination-flags/{id}")
    @Timed
    public ResponseEntity<ContaminationFlagDTO> getContaminationFlag(@PathVariable Long id) {
        log.debug("REST request to get ContaminationFlag : {}", id);
        ContaminationFlagDTO contaminationFlagDTO = contaminationFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contaminationFlagDTO));
    }

    /**
     * DELETE  /contamination-flags/:id : delete the "id" contaminationFlag.
     *
     * @param id the id of the contaminationFlagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contamination-flags/{id}")
    @Timed
    public ResponseEntity<Void> deleteContaminationFlag(@PathVariable Long id) {
        log.debug("REST request to delete ContaminationFlag : {}", id);
        contaminationFlagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
