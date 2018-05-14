package com.github.netstart.zssn.service;

import com.github.netstart.zssn.domain.Survivor;
import com.github.netstart.zssn.repository.SurvivorRepository;
import com.github.netstart.zssn.service.dto.SurvivorDTO;
import com.github.netstart.zssn.service.mapper.SurvivorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Survivor.
 */
@Service
@Transactional
public class SurvivorService {

    private final Logger log = LoggerFactory.getLogger(SurvivorService.class);

    private final SurvivorRepository survivorRepository;

    private final SurvivorMapper survivorMapper;

    public SurvivorService(SurvivorRepository survivorRepository, SurvivorMapper survivorMapper) {
        this.survivorRepository = survivorRepository;
        this.survivorMapper = survivorMapper;
    }

    /**
     * Save a survivor.
     *
     * @param survivorDTO the entity to save
     * @return the persisted entity
     */
    public SurvivorDTO save(SurvivorDTO survivorDTO) {
        log.debug("Request to save Survivor : {}", survivorDTO);
        Survivor survivor = survivorMapper.toEntity(survivorDTO);
        survivor = survivorRepository.save(survivor);
        return survivorMapper.toDto(survivor);
    }

    /**
     * Get all the survivors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SurvivorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Survivors");
        return survivorRepository.findAll(pageable)
            .map(survivorMapper::toDto);
    }

    /**
     * Get one survivor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public SurvivorDTO findOne(Long id) {
        log.debug("Request to get Survivor : {}", id);
        Survivor survivor = survivorRepository.findOne(id);
        return survivorMapper.toDto(survivor);
    }

    /**
     * Delete the survivor by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Survivor : {}", id);
        survivorRepository.delete(id);
    }
}
