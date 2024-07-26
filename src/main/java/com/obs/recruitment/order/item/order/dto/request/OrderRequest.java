package com.obs.recruitment.order.item.order.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderRequest {
    private String orderNo;
    private Integer itemId;
    private Integer qty;
}
