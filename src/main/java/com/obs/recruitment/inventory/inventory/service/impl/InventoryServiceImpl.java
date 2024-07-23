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
    private final InventoryRepository inventoryRepository;
    private final InventorySpecification inventorySpecification;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public void delete(Integer id) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByIdAndIsDeleted(id, false);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setDeleted(true);
            inventoryRepository.save(inventory);
        } else {
            throw new DataNotFoundException("Inventory not found" + id);
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
    public void update(Integer id, InventoryRequest inventoryRequest) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByIdAndIsDeleted(id, false);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            Inventory inventoryMapped = inventoryMapper.mapToInventory(inventory, inventoryRequest);
            inventoryRepository.save(inventoryMapped);
        } else {
            throw new DataNotFoundException("Inventory not found" + id);
        }
    }

    @Override
    public InventoryResponse get(Integer id) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByIdAndIsDeleted(id, false);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            return inventoryMapper.mapToInventoryResponse(inventory);
        } else {
            throw new DataNotFoundException("Inventory not found" + id);
        }
    }

    @Override
    public void create(InventoryRequest inventoryRequest) {
        Inventory inventoryMapped = inventoryMapper.mapToInventory(inventoryRequest);
        inventoryRepository.save(inventoryMapped);
    }
}
