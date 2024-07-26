package com.obs.recruitment.order.item.inventory.service.impl;

import com.obs.recruitment.order.item.inventory.dto.request.InventoryRequest;
import com.obs.recruitment.order.item.inventory.dto.response.InventoryResponse;
import com.obs.recruitment.order.item.inventory.mapper.InventoryMapper;
import com.obs.recruitment.order.item.inventory.model.Inventory;
import com.obs.recruitment.order.item.inventory.repository.InventoryRepository;
import com.obs.recruitment.order.item.inventory.repository.specification.InventorySpecification;
import com.obs.recruitment.order.item.inventory.service.InventoryService;
import com.obs.recruitment.order.item.item.model.Item;
import com.obs.recruitment.order.item.item.service.ItemService;
import com.obs.recruitment.order.item.shared.exception.DataNotFoundException;
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
    public static final String INVENTORY_NOT_FOUND = "Inventory not found";
    private final InventoryRepository inventoryRepository;
    private final InventorySpecification inventorySpecification;
    private final InventoryMapper inventoryMapper;
    private final ItemService itemService;

    @Override
    @Transactional
    public void delete(Integer id) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByIdAndIsDeleted(id, false);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setDeleted(true);
            inventoryRepository.save(inventory);
        } else {
            throw new DataNotFoundException(INVENTORY_NOT_FOUND + id);
        }
    }


    @Override
    public Page<InventoryResponse> getAll(Pageable pageable) {
        Page<Inventory> inventoryPage = inventoryRepository.findAll(inventorySpecification.findAllPredicate(), pageable);
        List<InventoryResponse> inventoryResponses = inventoryPage.getContent().stream().map(inventoryMapper::mapToInventoryResponse)
                .toList();
        return new PageImpl<>(
                inventoryResponses,
                inventoryPage.getPageable(),
                inventoryPage.getTotalElements()
        );
    }


    @Override
    public InventoryResponse get(Integer id) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByIdAndIsDeleted(id, false);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            return inventoryMapper.mapToInventoryResponse(inventory);
        } else {
            throw new DataNotFoundException(INVENTORY_NOT_FOUND + id);
        }
    }

    @Override
    public void create(InventoryRequest inventoryRequest) {
        Inventory inventoryMapped = inventoryMapper.mapToInventory(inventoryRequest);
        Integer itemId = inventoryRequest.getItemId();
        String type = inventoryRequest.getType();

        Item item = itemService.get(itemId);
        Integer quantity = inventoryRequest.getQty();

        saveInventoryAndUpdateStock(type, item, quantity, inventoryMapped);
    }

    @Override
    public void update(Integer id, InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.findByIdAndIsDeleted(id, false)
                .orElseThrow(() -> new DataNotFoundException(INVENTORY_NOT_FOUND + id));

        Integer qtyRequest = inventoryRequest.getQty();
        Integer qtyInventory = inventory.getQty();
        Inventory inventoryMapped = inventoryMapper.mapToInventory(inventoryRequest);

        Integer itemId = inventoryRequest.getItemId();
        String type = inventoryRequest.getType();

        Item item = itemService.get(itemId);

        Integer quantity = qtyRequest > qtyInventory ? qtyRequest - qtyInventory : qtyInventory - qtyRequest;

        saveInventoryAndUpdateStock(type, item, quantity, inventoryMapped);
    }

    private void saveInventoryAndUpdateStock(String type, Item item, Integer quantity, Inventory inventoryMapped) {
        if ("W".equals(type)) {
            if (item.getStock() > 0) {
                item.setStock(item.getStock() - quantity);
            }
        } else if ("T".equals(type)) {
            item.setStock(item.getStock() + quantity);
        }

        itemService.upsert(item);
        inventoryRepository.save(inventoryMapped);
    }
}
