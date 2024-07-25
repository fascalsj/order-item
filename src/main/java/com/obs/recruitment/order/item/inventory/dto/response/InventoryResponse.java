package com.obs.recruitment.order.item.inventory.dto.response;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class InventoryResponse {
    private Integer id;
    private Integer itemId;
    private Integer qty;
    private String type;
}