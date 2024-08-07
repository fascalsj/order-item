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
import com.obs.recruitment.order.item.order.service.OrderService;
import com.obs.recruitment.order.item.shared.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    public static final String ORDER_NOT_FOUND = "Order not found ";
    private final OrderRepository orderRepository;
    private final OrderSeqRepository orderSeqRepository;
    private final OrderSpecification orderSpecification;
    private final OrderMapper orderMapper;
    private final ItemService itemService;

    @Override
    @Transactional
    public void delete(String orderId) {
        Optional<Orders> orderOpt = orderRepository.findByOrderNoAndIsDeleted(orderId, false);
        if (orderOpt.isPresent()) {
            Orders order = orderOpt.get();
            order.setDeleted(true);
            orderRepository.save(order);
        } else {
            throw new DataNotFoundException(ORDER_NOT_FOUND + orderId);
        }
    }


    @Override
    public Page<OrderResponse> getAll(Pageable pageable ) {
        Specification<Orders> allPredicate = orderSpecification.findAllPredicate();
        Page<Orders> orderPage = orderRepository.findAll(allPredicate, pageable);
        List<OrderResponse> orderResponses = orderPage.getContent().stream().map(orderMapper::mapToOrderResponse)
                .toList();
        return new PageImpl<>(
                orderResponses,
                orderPage.getPageable(),
                orderPage.getTotalElements()
        );
    }

    @Override
    public void update(String orderId, OrderRequest orderRequest) {

        Optional<Orders> orderOpt = orderRepository.findByOrderNoAndIsDeleted(orderId, false);
        if (orderOpt.isPresent()) {
            Orders order = orderOpt.get();

            Integer qty = order.getQty();
            Integer qtyRequest = orderRequest.getQty();

            orderRequest.setOrderNo(orderId);
            Orders orderMapped = orderMapper.mapToOrder(order, orderRequest);

            Integer quantity = qtyRequest >= qty ? qtyRequest - qty : qty - qtyRequest;
            Item item = itemService.get(order.getItemId());
            item.setStock(item.getStock() - quantity);

            orderRepository.save(orderMapped);
        } else {
            throw new DataNotFoundException(ORDER_NOT_FOUND + orderId);
        }
    }

    @Override
    public OrderResponse get(String orderId) {
        Optional<Orders> orderOpt = orderRepository.findByOrderNoAndIsDeleted(orderId, false);
        if (orderOpt.isPresent()) {
            Orders order = orderOpt.get();
            return orderMapper.mapToOrderResponse(order);
        } else {
            throw new DataNotFoundException(ORDER_NOT_FOUND + orderId);
        }
    }

    @Override
    @Transactional
    public void create(OrderRequest orderRequest) {
        Integer itemId = orderRequest.getItemId();
        Orders orderMapped = orderMapper.mapToOrder(orderRequest);

        OrderSeq orderSeq = orderSeqRepository.findAll().getFirst();
        Integer orderIdInc = orderSeq.getOrderIdInc();
        int increment = orderIdInc + 1;

        orderSeq.setOrderIdInc(increment);
        orderSeqRepository.save(orderSeq);

        Item item = itemService.get(itemId);
        item.setStock(item.getStock() - orderRequest.getQty());

        orderMapped.setOrderNo("O" + increment);
        orderRepository.save(orderMapped);
    }
}
