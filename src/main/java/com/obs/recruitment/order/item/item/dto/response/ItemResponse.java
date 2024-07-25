package com.obs.recruitment.order.item.item.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ItemResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
}
