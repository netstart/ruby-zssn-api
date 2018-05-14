package com.github.netstart.zssn.service;

import com.github.netstart.zssn.domain.ContaminationFlag;
import com.github.netstart.zssn.repository.ContaminationFlagRepository;
import com.github.netstart.zssn.service.dto.ContaminationFlagDTO;
import com.github.netstart.zssn.service.mapper.ContaminationFlagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ContaminationFlag.
 */
@Service
@Transactional
public class ContaminationFlagService {

    private final Logger log = LoggerFactory.getLogger(ContaminationFlagService.class);

    private final ContaminationFlagRepository contaminationFlagRepository;

    private final ContaminationFlagMapper contaminationFlagMapper;

    public ContaminationFlagService(ContaminationFlagRepository contaminationFlagRepository, ContaminationFlagMapper contaminationFlagMapper) {
        this.contaminationFlagRepository = contaminationFlagRepository;
        this.contaminationFlagMapper = contaminationFlagMapper;
    }

    /**
     * Save a contaminationFlag.
     *
     * @param contaminationFlagDTO the entity to save
     * @return the persisted entity
     */
    public ContaminationFlagDTO save(ContaminationFlagDTO contaminationFlagDTO) {
        log.debug("Request to save ContaminationFlag : {}", contaminationFlagDTO);
        ContaminationFlag contaminationFlag = contaminationFlagMapper.toEntity(contaminationFlagDTO);
        contaminationFlag = contaminationFlagRepository.save(contaminationFlag);
        return contaminationFlagMapper.toDto(contaminationFlag);
    }

    /**
     * Get all the contaminationFlags.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContaminationFlagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContaminationFlags");
        return contaminationFlagRepository.findAll(pageable)
            .map(contaminationFlagMapper::toDto);
    }

    /**
     * Get one contaminationFlag by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ContaminationFlagDTO findOne(Long id) {
        log.debug("Request to get ContaminationFlag : {}", id);
        ContaminationFlag contaminationFlag = contaminationFlagRepository.findOne(id);
        return contaminationFlagMapper.toDto(contaminationFlag);
    }

    /**
     * Delete the contaminationFlag by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ContaminationFlag : {}", id);
        contaminationFlagRepository.delete(id);
    }
}
