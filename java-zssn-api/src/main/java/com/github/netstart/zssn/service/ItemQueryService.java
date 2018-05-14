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

import com.github.netstart.zssn.domain.Item;
import com.github.netstart.zssn.domain.*; // for static metamodels
import com.github.netstart.zssn.repository.ItemRepository;
import com.github.netstart.zssn.service.dto.ItemCriteria;

import com.github.netstart.zssn.service.dto.ItemDTO;
import com.github.netstart.zssn.service.mapper.ItemMapper;

/**
 * Service for executing complex queries for Item entities in the database.
 * The main input is a {@link ItemCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ItemDTO} or a {@link Page} of {@link ItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ItemQueryService extends QueryService<Item> {

    private final Logger log = LoggerFactory.getLogger(ItemQueryService.class);


    private final ItemRepository itemRepository;

    private final ItemMapper itemMapper;

    public ItemQueryService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    /**
     * Return a {@link List} of {@link ItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ItemDTO> findByCriteria(ItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Item> specification = createSpecification(criteria);
        return itemMapper.toDto(itemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ItemDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemDTO> findByCriteria(ItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Item> specification = createSpecification(criteria);
        final Page<Item> result = itemRepository.findAll(specification, page);
        return result.map(itemMapper::toDto);
    }

    /**
     * Function to convert ItemCriteria to a {@link Specifications}
     */
    private Specifications<Item> createSpecification(ItemCriteria criteria) {
        Specifications<Item> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Item_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Item_.name));
            }
            if (criteria.getPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoint(), Item_.point));
            }
            if (criteria.getInventoryId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getInventoryId(), Item_.inventory, Inventory_.id));
            }
        }
        return specification;
    }

}
