package com.obs.recruitment.order.item.order.service.impl;

import com.obs.recruitment.order.item.item.model.Item;
import com.obs.recruitment.order.item.item.service.ItemService;
import com.obs.recruitment.order.item.order.dto.request.OrderRequest;
import com.obs.recruitment.order.item.order.dto.response.OrderResponse;
import com.obs.recruitment.order.item.order.mapper.OrderMapper;
import com.obs.recruitment.order.item.order.model.OrderSeq;
import com.obs.recruitment.order.item.order.model.Orders;
import com.obs.recruitment.order.item.order.repository.OrderRepository;
import com.obs.recruitment.order.item.order.repository.OrderSeqRepository;
import com.obs.recruitment.order.item.order.repository.specification.OrderSpecification;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderSeqRepository orderSeqRepository;


    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ItemService itemService;

    @Mock
    OrderSpecification orderSpecification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteOrder_whenOrderExists() {
        String orderId = "123";
        Orders order = new Orders();
        when(orderRepository.findByOrderNoAndIsDeleted(orderId, false)).thenReturn(Optional.of(order));

        orderService.delete(orderId);

        verify(orderRepository, times(1)).save(order);
        assertTrue(order.isDeleted());
    }

    @Test
    void testDeleteOrder_whenOrderDoesNotExist() {
        String orderId = "123";
        when(orderRepository.findByOrderNoAndIsDeleted(orderId, false)).thenReturn(Optional.empty());

        DataNotFoundException thrown = assertThrows(DataNotFoundException.class, () -> {
            orderService.delete(orderId);
        });

        assertEquals("Order not found 123", thrown.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testGetAllOrders() {


        Pageable pageable = PageRequest.of(0, 5);
        Orders order = new Orders();
        Page<Orders> orderPage = new PageImpl<>(List.of(order));

        OrderResponse orderResponse = new OrderResponse();

        Specification<Orders> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };

        when(orderSpecification.findAllPredicate()).thenReturn(specification);

        when(orderRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(orderPage);

        when(orderMapper.mapToOrderResponse(order)).thenReturn(orderResponse);

        Page<OrderResponse> result = orderService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(orderResponse, result.getContent().getFirst());
    }

    @Test
    void testUpdateOrder_whenOrderExists() {
        String orderId = "123";
        Orders order = new Orders();
        order.setQty(1);
        order.setItemId(1);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setQty(1);
        orderRequest.setItemId(1);
        orderRequest.setOrderNo("O1");
        Item item = new Item();
        item.setStock(100);
        item.setId(1);
        when(orderRepository.findByOrderNoAndIsDeleted(orderId, false)).thenReturn(Optional.of(order));
        when(orderMapper.mapToOrder(order, orderRequest)).thenReturn(order);
        when(itemService.get(anyInt())).thenReturn(item);

        orderService.update(orderId, orderRequest);

        verify(orderRepository, times(1)).save(order);
        verify(itemService, times(1)).get(anyInt());
    }

    @Test
    void testUpdateOrder_whenOrderDoesNotExist() {
        String orderId = "123";
        OrderRequest orderRequest = new OrderRequest();
        when(orderRepository.findByOrderNoAndIsDeleted(orderId, false)).thenReturn(Optional.empty());

        DataNotFoundException thrown = assertThrows(DataNotFoundException.class, () -> {
            orderService.update(orderId, orderRequest);
        });

        assertEquals("Order not found 123", thrown.getMessage());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testGetOrder_whenOrderExists() {
        String orderId = "123";
        Orders order = new Orders();
        OrderResponse orderResponse = new OrderResponse();
        when(orderRepository.findByOrderNoAndIsDeleted(orderId, false)).thenReturn(Optional.of(order));
        when(orderMapper.mapToOrderResponse(order)).thenReturn(orderResponse);

        OrderResponse result = orderService.get(orderId);

        assertNotNull(result);
        assertEquals(orderResponse, result);
    }

    @Test
    void testGetOrder_whenOrderDoesNotExist() {
        String orderId = "123";
        when(orderRepository.findByOrderNoAndIsDeleted(orderId, false)).thenReturn(Optional.empty());

        DataNotFoundException thrown = assertThrows(DataNotFoundException.class, () -> {
            orderService.get(orderId);
        });

        assertEquals("Order not found 123", thrown.getMessage());
    }

    @Test
    void testCreateOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setItemId(1);
        orderRequest.setQty(12);
        OrderSeq orderSeq = new OrderSeq();
        orderSeq.setId(1);
        orderSeq.setOrderIdInc(12);
        Item item = new Item();
        item.setId(1);
        item.setStock(100);
        Orders order = new Orders();
        when(orderSeqRepository.findAll()).thenReturn(List.of(orderSeq));
        when(orderMapper.mapToOrder(orderRequest)).thenReturn(order);
        when(itemService.get(anyInt())).thenReturn(item);


        orderService.create(orderRequest);

        verify(orderRepository, times(1)).save(order);
        assertNotNull(order);
    }
}