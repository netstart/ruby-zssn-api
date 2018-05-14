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

import com.github.netstart.zssn.domain.ContaminationFlag;
import com.github.netstart.zssn.domain.*; // for static metamodels
import com.github.netstart.zssn.repository.ContaminationFlagRepository;
import com.github.netstart.zssn.service.dto.ContaminationFlagCriteria;

import com.github.netstart.zssn.service.dto.ContaminationFlagDTO;
import com.github.netstart.zssn.service.mapper.ContaminationFlagMapper;

/**
 * Service for executing complex queries for ContaminationFlag entities in the database.
 * The main input is a {@link ContaminationFlagCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContaminationFlagDTO} or a {@link Page} of {@link ContaminationFlagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContaminationFlagQueryService extends QueryService<ContaminationFlag> {

    private final Logger log = LoggerFactory.getLogger(ContaminationFlagQueryService.class);


    private final ContaminationFlagRepository contaminationFlagRepository;

    private final ContaminationFlagMapper contaminationFlagMapper;

    public ContaminationFlagQueryService(ContaminationFlagRepository contaminationFlagRepository, ContaminationFlagMapper contaminationFlagMapper) {
        this.contaminationFlagRepository = contaminationFlagRepository;
        this.contaminationFlagMapper = contaminationFlagMapper;
    }

    /**
     * Return a {@link List} of {@link ContaminationFlagDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContaminationFlagDTO> findByCriteria(ContaminationFlagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ContaminationFlag> specification = createSpecification(criteria);
        return contaminationFlagMapper.toDto(contaminationFlagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContaminationFlagDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaminationFlagDTO> findByCriteria(ContaminationFlagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ContaminationFlag> specification = createSpecification(criteria);
        final Page<ContaminationFlag> result = contaminationFlagRepository.findAll(specification, page);
        return result.map(contaminationFlagMapper::toDto);
    }

    /**
     * Function to convert ContaminationFlagCriteria to a {@link Specifications}
     */
    private Specifications<ContaminationFlag> createSpecification(ContaminationFlagCriteria criteria) {
        Specifications<ContaminationFlag> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ContaminationFlag_.id));
            }
            if (criteria.getReportedById() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReportedById(), ContaminationFlag_.reportedBy, Survivor_.id));
            }
            if (criteria.getReportedId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReportedId(), ContaminationFlag_.reported, Survivor_.id));
            }
        }
        return specification;
    }

}
