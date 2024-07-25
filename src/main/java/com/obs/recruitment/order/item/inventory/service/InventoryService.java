package com.obs.recruitment.order.item.inventory.service;

import com.obs.recruitment.order.item.inventory.dto.request.InventoryRequest;
import com.obs.recruitment.order.item.inventory.dto.response.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface InventoryService {

    void delete(Integer id);

    Page<InventoryResponse> getAll(Pageable pageable);

    void update(Integer id, InventoryRequest itemRequest);

    InventoryResponse get(Integer id);
    void create(InventoryRequest itemRequest);
}
