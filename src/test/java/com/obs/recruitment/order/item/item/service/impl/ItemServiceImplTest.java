package com.obs.recruitment.order.item.item.service.impl;

import com.obs.recruitment.order.item.item.dto.request.ItemRequest;
import com.obs.recruitment.order.item.item.dto.response.ItemResponse;
import com.obs.recruitment.order.item.item.mapper.ItemMapper;
import com.obs.recruitment.order.item.item.model.Item;
import com.obs.recruitment.order.item.item.repository.ItemRepository;
import com.obs.recruitment.order.item.item.repository.specification.ItemSpecification;
import com.obs.recruitment.order.item.shared.exception.DataNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.jeasy.random.EasyRandom;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemSpecification itemSpecification;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void delete_ShouldSetItemAsDeleted() {
        // Arrange
        Integer itemId = 1;
        Item item = new Item();
        item.setId(itemId);
        when(itemRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.of(item));

        // Act
        itemService.delete(itemId);

        // Assert
        assertTrue(item.isDeleted());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void delete_ShouldThrowDataNotFoundException_WhenItemNotFound() {
        // Arrange
        Integer itemId = 1;
        when(itemRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(DataNotFoundException.class, () -> itemService.delete(itemId));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void getAll_ShouldReturnPageOfItemResponses() {
        EasyRandom easyRandom = new EasyRandom();

        Pageable pageable = PageRequest.of(0, 10);
        String search = "search";
        ItemResponse itemResponse = new ItemResponse();

        Specification<Item> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };

        List<Item> items = easyRandom.objects(Item.class, 1)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList));
        Page<Item> itemPage = new PageImpl<>(items);
        when(itemSpecification.findAllPredicate(anyString())).thenReturn(specification);
        when(itemRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(itemPage);

        when(itemMapper.mapToItemResponse(any())).thenReturn(itemResponse);

        // Act
        Page<ItemResponse> result = itemService.getAll(pageable, search);

        // Assert
        assertEquals(items.size(), result.getContent().size());
        verify(itemRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void update_ShouldUpdateItem() {
        // Arrange
        Integer itemId = 1;
        ItemRequest itemRequest = new ItemRequest();
        Item item = new Item();
        when(itemRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.of(item));

        // Act
        itemService.update(itemId, itemRequest);

        // Assert
        verify(itemMapper, times(1)).mapToItem(any(), any());
        verify(itemRepository, times(1)).save(any());
    }

    @Test
    void update_ShouldThrowDataNotFoundException_WhenItemNotFound() {
        // Arrange
        Integer itemId = 1;
        ItemRequest itemRequest = new ItemRequest();
        when(itemRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(DataNotFoundException.class, () -> itemService.update(itemId, itemRequest));
        verify(itemRepository, never()).save(any());
    }

    @Test
    void get_ShouldReturnItemResponse() {
        ItemResponse itemResponse = new ItemResponse();
        // Arrange
        Integer itemId = 1;
        Item item = new Item();
        when(itemRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.of(item));

        when(itemMapper.mapToItemResponse(any())).thenReturn(itemResponse);

        // Act
        ItemResponse result = itemService.get(itemId);

        // Assert
        assertNotNull(result);
        verify(itemMapper, times(1)).mapToItemResponse(any());
    }

    @Test
    void get_ShouldThrowDataNotFoundException_WhenItemNotFound() {
        // Arrange
        Integer itemId = 1;
        when(itemRepository.findByIdAndIsDeleted(anyInt(), anyBoolean())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(DataNotFoundException.class, () -> itemService.get(itemId));
    }

    @Test
    void create_ShouldSaveNewItem() {
        // Arrange
        ItemRequest itemRequest = new ItemRequest();
        Item itemMapped = new Item();
        when(itemMapper.mapToItem(any())).thenReturn(itemMapped);

        // Act
        itemService.create(itemRequest);

        // Assert
        verify(itemRepository, times(1)).save(any());
    }
}

