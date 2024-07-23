package com.obs.recruitment.inventory.inventory.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class InventoryRequest {
    private Integer id;
    private Integer itemId;
    private Integer qty;
    private String type;
}
