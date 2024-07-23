package com.obs.recruitment.inventory.inventory.mapper;

import com.obs.recruitment.inventory.inventory.dto.request.InventoryRequest;
import com.obs.recruitment.inventory.inventory.dto.response.InventoryResponse;
import com.obs.recruitment.inventory.inventory.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryResponse mapToInventoryResponse(Inventory item);

    @Named("mapEdit")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "item", ignore = true)
    Inventory mapToInventory(@MappingTarget Inventory inventory, InventoryRequest itemRequest);

    @Named("mapCreate")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "item", ignore = true)
    Inventory mapToInventory(InventoryRequest inventoryRequest);

}
