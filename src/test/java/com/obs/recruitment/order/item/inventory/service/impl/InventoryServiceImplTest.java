package com.obs.recruitment.order.item.inventory.service.impl;

import com.obs.recruitment.order.item.inventory.dto.request.InventoryRequest;
import com.obs.recruitment.order.item.inventory.dto.response.InventoryResponse;
import com.obs.recruitment.order.item.inventory.mapper.InventoryMapper;
import com.obs.recruitment.order.item.inventory.model.Inventory;
import com.obs.recruitment.order.item.inventory.repository.InventoryRepository;
import com.obs.recruitment.order.item.inventory.repository.specification.InventorySpecification;
import com.obs.recruitment.order.item.shared.exception.DataNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventorySpecification inventorySpecification;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deleteExistingInventory() {
        // Mock data
        Integer inventoryId = 1;
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);
        when(inventoryRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.of(inventory));

        // Call service method
        assertDoesNotThrow(() -> inventoryService.delete(inventoryId));

        // Verify
        assertTrue(inventory.isDeleted());
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    public void deleteNonExistingInventory() {
        // Mock data
        Integer inventoryId = 1;
        when(inventoryRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(DataNotFoundException.class, () -> inventoryService.delete(inventoryId));

        // Verify
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    public void getAllInventory() {
        // Mock data
        Pageable pageable = PageRequest.of(0, 5);
        List<Inventory> inventoryList = List.of(new Inventory(), new Inventory());
        Page<Inventory> inventoryPage = new PageImpl<>(inventoryList, pageable, inventoryList.size());

        Specification<Inventory> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };

        when(inventorySpecification.findAllPredicate()).thenReturn(specification);

        when(inventoryRepository.findAll(any(Specification.class), any(PageRequest.class)))
                .thenReturn(inventoryPage);


        // Call service method
        Page<InventoryResponse> result = inventoryService.getAll(pageable);

        // Verify
        assertEquals(inventoryList.size(), result.getContent().size());
        verify(inventoryMapper, times(inventoryList.size())).mapToInventoryResponse(any());
    }

    @Test
    public void updateExistingInventory() {
        // Mock data
        Integer inventoryId = 1;
        InventoryRequest request = new InventoryRequest();
        Inventory inventory = new Inventory();
        when(inventoryRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.of(inventory));
        when(inventoryMapper.mapToInventory(any(), any())).thenReturn(inventory);

        // Call service method
        assertDoesNotThrow(() -> inventoryService.update(inventoryId, request));

        // Verify
        verify(inventoryRepository, times(1)).save(any());
    }

    @Test
    public void updateNonExistingInventory() {
        // Mock data
        Integer inventoryId = 1;
        InventoryRequest request = new InventoryRequest();
        when(inventoryRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(DataNotFoundException.class, () -> inventoryService.update(inventoryId, request));

        // Verify
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    public void getExistingInventory() {
        // Mock data
        Integer inventoryId = 1;
        Inventory inventory = new Inventory();
        when(inventoryRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.of(inventory));
        when(inventoryMapper.mapToInventoryResponse(any())).thenReturn(new InventoryResponse());

        // Call service method
        InventoryResponse result = inventoryService.get(inventoryId);

        // Verify
        assertNotNull(result);
    }

    @Test
    public void getNonExistingInventory() {
        // Mock data
        Integer inventoryId = 1;
        when(inventoryRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(DataNotFoundException.class, () -> inventoryService.get(inventoryId));
    }

    @Test
    public void createInventory() {
        // Mock data
        InventoryRequest request = new InventoryRequest();
        Inventory inventory = new Inventory();
        when(inventoryMapper.mapToInventory(any())).thenReturn(inventory);

        // Call service method
        assertDoesNotThrow(() -> inventoryService.create(request));

        // Verify
        verify(inventoryRepository, times(1)).save(any());
    }
}