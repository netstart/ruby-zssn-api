package com.github.netstart.zssn.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.github.netstart.zssn.domain.Survivor;
import com.github.netstart.zssn.domain.*; // for static metamodels
import com.github.netstart.zssn.repository.SurvivorRepository;
import com.github.netstart.zssn.service.dto.SurvivorCriteria;

import com.github.netstart.zssn.service.dto.SurvivorDTO;
import com.github.netstart.zssn.service.mapper.SurvivorMapper;

/**
 * Service for executing complex queries for Survivor entities in the database.
 * The main input is a {@link SurvivorCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SurvivorDTO} or a {@link Page} of {@link SurvivorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SurvivorQueryService extends QueryService<Survivor> {

    private final Logger log = LoggerFactory.getLogger(SurvivorQueryService.class);


    private final SurvivorRepository survivorRepository;

    private final SurvivorMapper survivorMapper;

    public SurvivorQueryService(SurvivorRepository survivorRepository, SurvivorMapper survivorMapper) {
        this.survivorRepository = survivorRepository;
        this.survivorMapper = survivorMapper;
    }

    /**
     * Return a {@link List} of {@link SurvivorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SurvivorDTO> findByCriteria(SurvivorCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Survivor> specification = createSpecification(criteria);
        return survivorMapper.toDto(survivorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SurvivorDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SurvivorDTO> findByCriteria(SurvivorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Survivor> specification = createSpecification(criteria);
        final Page<Survivor> result = survivorRepository.findAll(specification, page);
        return result.map(survivorMapper::toDto);
    }

    /**
     * Function to convert SurvivorCriteria to a {@link Specifications}
     */
    private Specifications<Survivor> createSpecification(SurvivorCriteria criteria) {
        Specifications<Survivor> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Survivor_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Survivor_.name));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), Survivor_.age));
            }
            if (criteria.getLastLocationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLastLocationId(), Survivor_.lastLocation, Location_.id));
            }
            if (criteria.getInventoryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInventoryId(), Survivor_.inventory, Inventory_.id));
            }
        }
        return specification;
    }

}
