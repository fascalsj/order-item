package com.obs.recruitment.inventory.inventory.service.impl;

import com.obs.recruitment.inventory.inventory.dto.request.InventoryRequest;
import com.obs.recruitment.inventory.inventory.dto.response.InventoryResponse;
import com.obs.recruitment.inventory.inventory.model.Inventory;
import com.obs.recruitment.inventory.inventory.repository.InventoryRepository;
import com.obs.recruitment.inventory.inventory.repository.specification.InventorySpecification;
import com.obs.recruitment.inventory.inventory.service.InventoryService;
import com.obs.recruitment.inventory.inventory.mapper.InventoryMapper;
import com.obs.recruitment.inventory.shared.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository itemRepository;
    private final InventorySpecification itemSpecification;
    private final InventoryMapper itemMapper;

    @Override
    @Transactional
    public void delete(Integer id) {
        Optional<Inventory> itemOpt = itemRepository.findByIdAndIsDeleted(id, false);
        if (itemOpt.isPresent()) {
            Inventory item = itemOpt.get();
            item.setDeleted(true);
            itemRepository.save(item);
        } else {
            throw new DataNotFoundException("Item not found" + id);
        }
    }


    @Override
    public Page<InventoryResponse> getAll(Pageable pageable) {
        Page<Inventory> itemPage = itemRepository.findAll(itemSpecification.findAllPredicate(), pageable);
        List<InventoryResponse> itemResponses = itemPage.getContent().stream().map(itemMapper::mapToItemResponse)
                .toList();
        return new PageImpl<>(
                itemResponses,
                itemPage.getPageable(),
                itemPage.getTotalElements()
        );
    }

    @Override
    public void update(Integer id, InventoryRequest itemRequest) {
        Optional<Inventory> itemOpt =itemRepository.findByIdAndIsDeleted(id, false);
        if (itemOpt.isPresent()) {
            Inventory item = itemOpt.get();
            Inventory itemMapped = itemMapper.mapToItem(item, itemRequest);
            itemRepository.save(itemMapped);
        } else {
            throw new DataNotFoundException("Item not found" + id);
        }
    }

    @Override
    public InventoryResponse get(Integer id) {
        Optional<Inventory> itemOpt = itemRepository.findByIdAndIsDeleted(id, false);
        if (itemOpt.isPresent()) {
            Inventory item = itemOpt.get();
            return itemMapper.mapToItemResponse(item);
        } else {
            throw new DataNotFoundException("Item not found" + id);
        }
    }

    @Override
    public void create(InventoryRequest itemRequest) {
        Inventory itemMapped = itemMapper.mapToItem(itemRequest);
        itemRepository.save(itemMapped);
    }
}
