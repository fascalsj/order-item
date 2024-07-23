package com.obs.recruitment.inventory.item.mapper;

import com.obs.recruitment.inventory.item.dto.request.ItemRequest;
import com.obs.recruitment.inventory.item.dto.response.ItemResponse;
import com.obs.recruitment.inventory.item.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemResponse mapToItemResponse(Item item);

    @Named("mapEdit")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "inventories", ignore = true)
    Item mapToItem(@MappingTarget Item item, ItemRequest itemRequest);

    @Named("mapCreate")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "inventories", ignore = true)
    Item mapToItem(ItemRequest itemRequest);

}
