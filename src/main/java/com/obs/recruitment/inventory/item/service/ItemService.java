package com.obs.recruitment.inventory.item.service;

import com.obs.recruitment.inventory.item.dto.request.ItemRequest;
import com.obs.recruitment.inventory.item.dto.response.ItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ItemService {

    void delete(Integer id);

    Page<ItemResponse> getAll(Pageable pageable, String search);

    void update(Integer id, ItemRequest itemRequest);

    ItemResponse get(Integer id);
    void create(ItemRequest itemRequest);
}
