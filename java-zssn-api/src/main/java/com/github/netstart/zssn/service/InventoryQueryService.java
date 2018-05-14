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

import com.github.netstart.zssn.domain.Inventory;
import com.github.netstart.zssn.domain.*; // for static metamodels
import com.github.netstart.zssn.repository.InventoryRepository;
import com.github.netstart.zssn.service.dto.InventoryCriteria;

import com.github.netstart.zssn.service.dto.InventoryDTO;
import com.github.netstart.zssn.service.mapper.InventoryMapper;

/**
 * Service for executing complex queries for Inventory entities in the database.
 * The main input is a {@link InventoryCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InventoryDTO} or a {@link Page} of {@link InventoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryQueryService extends QueryService<Inventory> {

    private final Logger log = LoggerFactory.getLogger(InventoryQueryService.class);


    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;

    public InventoryQueryService(InventoryRepository inventoryRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryMapper = inventoryMapper;
    }

    /**
     * Return a {@link List} of {@link InventoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InventoryDTO> findByCriteria(InventoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Inventory> specification = createSpecification(criteria);
        return inventoryMapper.toDto(inventoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InventoryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryDTO> findByCriteria(InventoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Inventory> specification = createSpecification(criteria);
        final Page<Inventory> result = inventoryRepository.findAll(specification, page);
        return result.map(inventoryMapper::toDto);
    }

    /**
     * Function to convert InventoryCriteria to a {@link Specifications}
     */
    private Specifications<Inventory> createSpecification(InventoryCriteria criteria) {
        Specifications<Inventory> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Inventory_.id));
            }
            if (criteria.getItensId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getItensId(), Inventory_.itens, Item_.id));
            }
        }
        return specification;
    }

}
