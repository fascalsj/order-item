package com.obs.recruitment.inventory.item.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ItemRequest {
    private Integer id;
    private String name;
    private BigDecimal price;
}
