package com.github.netstart.zssn.service.mapper;

import com.github.netstart.zssn.domain.*;
import com.github.netstart.zssn.service.dto.SurvivorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Survivor and its DTO SurvivorDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, InventoryMapper.class})
public interface SurvivorMapper extends EntityMapper<SurvivorDTO, Survivor> {

    @Mapping(source = "lastLocation.id", target = "lastLocationId")
    @Mapping(source = "inventory.id", target = "inventoryId")
    SurvivorDTO toDto(Survivor survivor);

    @Mapping(source = "lastLocationId", target = "lastLocation")
    @Mapping(source = "inventoryId", target = "inventory")
    Survivor toEntity(SurvivorDTO survivorDTO);

    default Survivor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Survivor survivor = new Survivor();
        survivor.setId(id);
        return survivor;
    }
}
