package com.github.netstart.zssn.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.netstart.zssn.service.SurvivorService;
import com.github.netstart.zssn.web.rest.errors.BadRequestAlertException;
import com.github.netstart.zssn.web.rest.util.HeaderUtil;
import com.github.netstart.zssn.web.rest.util.PaginationUtil;
import com.github.netstart.zssn.service.dto.SurvivorDTO;
import com.github.netstart.zssn.service.dto.SurvivorCriteria;
import com.github.netstart.zssn.service.SurvivorQueryService;
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
 * REST controller for managing Survivor.
 */
@RestController
@RequestMapping("/api")
public class SurvivorResource {

    private final Logger log = LoggerFactory.getLogger(SurvivorResource.class);

    private static final String ENTITY_NAME = "survivor";

    private final SurvivorService survivorService;

    private final SurvivorQueryService survivorQueryService;

    public SurvivorResource(SurvivorService survivorService, SurvivorQueryService survivorQueryService) {
        this.survivorService = survivorService;
        this.survivorQueryService = survivorQueryService;
    }

    /**
     * POST  /survivors : Create a new survivor.
     *
     * @param survivorDTO the survivorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new survivorDTO, or with status 400 (Bad Request) if the survivor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/survivors")
    @Timed
    public ResponseEntity<SurvivorDTO> createSurvivor(@Valid @RequestBody SurvivorDTO survivorDTO) throws URISyntaxException {
        log.debug("REST request to save Survivor : {}", survivorDTO);
        if (survivorDTO.getId() != null) {
            throw new BadRequestAlertException("A new survivor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SurvivorDTO result = survivorService.save(survivorDTO);
        return ResponseEntity.created(new URI("/api/survivors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /survivors : Updates an existing survivor.
     *
     * @param survivorDTO the survivorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated survivorDTO,
     * or with status 400 (Bad Request) if the survivorDTO is not valid,
     * or with status 500 (Internal Server Error) if the survivorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/survivors")
    @Timed
    public ResponseEntity<SurvivorDTO> updateSurvivor(@Valid @RequestBody SurvivorDTO survivorDTO) throws URISyntaxException {
        log.debug("REST request to update Survivor : {}", survivorDTO);
        if (survivorDTO.getId() == null) {
            return createSurvivor(survivorDTO);
        }
        SurvivorDTO result = survivorService.save(survivorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, survivorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /survivors : get all the survivors.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of survivors in body
     */
    @GetMapping("/survivors")
    @Timed
    public ResponseEntity<List<SurvivorDTO>> getAllSurvivors(SurvivorCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Survivors by criteria: {}", criteria);
        Page<SurvivorDTO> page = survivorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/survivors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /survivors/:id : get the "id" survivor.
     *
     * @param id the id of the survivorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the survivorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/survivors/{id}")
    @Timed
    public ResponseEntity<SurvivorDTO> getSurvivor(@PathVariable Long id) {
        log.debug("REST request to get Survivor : {}", id);
        SurvivorDTO survivorDTO = survivorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(survivorDTO));
    }

    /**
     * DELETE  /survivors/:id : delete the "id" survivor.
     *
     * @param id the id of the survivorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/survivors/{id}")
    @Timed
    public ResponseEntity<Void> deleteSurvivor(@PathVariable Long id) {
        log.debug("REST request to delete Survivor : {}", id);
        survivorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
