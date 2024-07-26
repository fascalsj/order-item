package com.obs.recruitment.order.item.inventory.service.impl;

import com.obs.recruitment.order.item.inventory.dto.request.InventoryRequest;
import com.obs.recruitment.order.item.inventory.dto.response.InventoryResponse;
import com.obs.recruitment.order.item.inventory.mapper.InventoryMapper;
import com.obs.recruitment.order.item.inventory.model.Inventory;
import com.obs.recruitment.order.item.inventory.repository.InventoryRepository;
import com.obs.recruitment.order.item.inventory.repository.specification.InventorySpecification;
import com.obs.recruitment.order.item.item.model.Item;
import com.obs.recruitment.order.item.item.service.ItemService;
import com.obs.recruitment.order.item.shared.exception.DataNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
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

class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventorySpecification inventorySpecification;

    @Mock
    private InventoryMapper inventoryMapper;

    @Mock
    private ItemService itemService;

    @Captor
    ArgumentCaptor<Item> itemArgumentCaptor;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void delete() {
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
    void delete_WhenNotExist_ThenThrow() {
        // Mock data
        Integer inventoryId = 1;
        when(inventoryRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(DataNotFoundException.class, () -> inventoryService.delete(inventoryId));

        // Verify
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void getAll() {
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
    void update_WhenWithDraw_Success() {
        Inventory inventory = new Inventory();
        inventory.setQty(20);

        InventoryRequest request = new InventoryRequest();
        request.setQty(5);
        request.setItemId(1);
        request.setType("W");
        when(inventoryRepository.findByIdAndIsDeleted(1, false)).thenReturn(Optional.of(inventory));
        when(inventoryMapper.mapToInventory(request)).thenReturn(inventory);

        Item item = new Item();
        item.setStock(20);
        when(itemService.get(1)).thenReturn(item);

        inventoryService.update(1, request);

        verify(inventoryRepository).save(inventory);
        verify(itemService).upsert(itemArgumentCaptor.capture());
        assertEquals(5, itemArgumentCaptor.getValue().getStock());
    }


    @Test
    void update_WhenTopup_Success() {
        Inventory inventory = new Inventory();
        inventory.setQty(20);

        InventoryRequest request = new InventoryRequest();
        request.setQty(5);
        request.setItemId(1);
        request.setType("T");
        when(inventoryRepository.findByIdAndIsDeleted(1, false)).thenReturn(Optional.of(inventory));
        when(inventoryMapper.mapToInventory(request)).thenReturn(inventory);

        Item item = new Item();
        item.setStock(20);
        when(itemService.get(1)).thenReturn(item);

        inventoryService.update(1, request);

        verify(inventoryRepository).save(inventory);
        verify(itemService).upsert(itemArgumentCaptor.capture());
        assertEquals(25, itemArgumentCaptor.getValue().getStock());
    }

    @Test
    void update_WhenNotExist_ThenThrow() {
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
    void get() {
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
    void get_WhenNotExist_ThenThrow() {
        // Mock data
        Integer inventoryId = 1;
        when(inventoryRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.empty());

        // Call service method and assert exception
        assertThrows(DataNotFoundException.class, () -> inventoryService.get(inventoryId));
    }

    @Test
    void create_WhenTopUp_Success() {
        InventoryRequest request = new InventoryRequest();
        request.setQty(10);
        request.setItemId(1);
        request.setType("T");
        Inventory inventory = new Inventory();
        when(inventoryMapper.mapToInventory(request)).thenReturn(inventory);

        Item item = new Item();
        item.setStock(5);
        when(itemService.get(1)).thenReturn(item);

        inventoryService.create(request);

        verify(inventoryRepository).save(inventory);
        verify(itemService).upsert(item);
        assertEquals(15, item.getStock());
    }


    @Test
    void create_WhenWithDraw_Success() {
        InventoryRequest request = new InventoryRequest();
        request.setQty(10);
        request.setItemId(1);
        request.setType("T");
        Inventory inventory = new Inventory();
        when(inventoryMapper.mapToInventory(request)).thenReturn(inventory);

        Item item = new Item();
        item.setStock(5);
        when(itemService.get(1)).thenReturn(item);

        inventoryService.create(request);

        verify(inventoryRepository).save(inventory);
        verify(itemService).upsert(item);
        assertEquals(15, item.getStock());
    }
}