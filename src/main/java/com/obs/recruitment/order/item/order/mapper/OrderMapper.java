package com.obs.recruitment.order.item.order.mapper;

import com.obs.recruitment.order.item.order.dto.request.OrderRequest;
import com.obs.recruitment.order.item.order.dto.response.OrderResponse;
import com.obs.recruitment.order.item.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse mapToOrderResponse(Order order);

    @Named("mapEdit")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "item", ignore = true)
    Order mapToOrder(@MappingTarget Order order, OrderRequest orderRequest);

    @Named("mapCreate")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "item", ignore = true)
    Order mapToOrder(OrderRequest orderRequest);

}
