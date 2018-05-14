package com.github.netstart.zssn.service.mapper;

import com.github.netstart.zssn.domain.*;
import com.github.netstart.zssn.service.dto.ItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Item and its DTO ItemDTO.
 */
@Mapper(componentModel = "spring", uses = {InventoryMapper.class})
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {

    @Mapping(source = "inventory.id", target = "inventoryId")
    ItemDTO toDto(Item item);

    @Mapping(source = "inventoryId", target = "inventory")
    Item toEntity(ItemDTO itemDTO);

    default Item fromId(Long id) {
        if (id == null) {
            return null;
        }
        Item item = new Item();
        item.setId(id);
        return item;
    }
}
