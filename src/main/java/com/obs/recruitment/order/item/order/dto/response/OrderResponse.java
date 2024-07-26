package com.obs.recruitment.order.item.order.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OrderResponse {
    private String orderNo;
    private Integer itemId;
    private BigDecimal qty;
}
