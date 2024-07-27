package com.obs.recruitment.order.item.order.service;

import com.obs.recruitment.order.item.order.dto.request.OrderRequest;
import com.obs.recruitment.order.item.order.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderService {

    void delete(String id);

    Page<OrderResponse> getAll(Pageable pageable);

    void update(String id, OrderRequest orderRequest);

    OrderResponse get(String id);

    void create(OrderRequest orderRequest);
}
