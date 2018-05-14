package com.github.netstart.zssn.service.mapper;

import com.github.netstart.zssn.domain.*;
import com.github.netstart.zssn.service.dto.ContaminationFlagDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ContaminationFlag and its DTO ContaminationFlagDTO.
 */
@Mapper(componentModel = "spring", uses = {SurvivorMapper.class})
public interface ContaminationFlagMapper extends EntityMapper<ContaminationFlagDTO, ContaminationFlag> {

    @Mapping(source = "reportedBy.id", target = "reportedById")
    @Mapping(source = "reported.id", target = "reportedId")
    ContaminationFlagDTO toDto(ContaminationFlag contaminationFlag);

    @Mapping(source = "reportedById", target = "reportedBy")
    @Mapping(source = "reportedId", target = "reported")
    ContaminationFlag toEntity(ContaminationFlagDTO contaminationFlagDTO);

    default ContaminationFlag fromId(Long id) {
        if (id == null) {
            return null;
        }
        ContaminationFlag contaminationFlag = new ContaminationFlag();
        contaminationFlag.setId(id);
        return contaminationFlag;
    }
}
