package com.obs.recruitment.inventory.inventory.dto.response;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class InventoryResponse {
    private Integer id;
    private Integer qty;
    private String type;
}
