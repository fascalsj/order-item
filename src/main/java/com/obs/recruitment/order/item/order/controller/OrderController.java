package com.obs.recruitment.order.item.order.controller;

import com.obs.recruitment.order.item.order.dto.request.OrderRequest;
import com.obs.recruitment.order.item.order.dto.response.OrderResponse;
import com.obs.recruitment.order.item.order.service.OrderService;
import com.obs.recruitment.order.item.shared.dto.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @DeleteMapping("{orderId}")
    ResponseEntity<GlobalResponse<Object>> delete(@PathVariable("orderId") String orderId) {
        orderService.delete(orderId);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Item has deleted")
                .build());
    }

    @GetMapping()
    ResponseEntity<GlobalResponse<Object>> getAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "5") int size,
                                                  @RequestParam(name = "search", defaultValue = "") String search) {
        Page<OrderResponse> orderResponses = orderService.getAll(PageRequest.of(page, size), search);
        return ResponseEntity.ok().body(
                GlobalResponse.builder()
                        .code(HttpStatus.OK.value())
                        .result(orderResponses)
                        .build()
        );
    }

    @PostMapping()
    ResponseEntity<GlobalResponse<Object>> create(@RequestBody OrderRequest orderRequest) {
        orderService.create(orderRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(GlobalResponse.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Item has been created")
                        .build());
    }

    @PutMapping("{orderId}")
    ResponseEntity<GlobalResponse<Object>> update(@PathVariable("orderId") String orderId,
                                                  @RequestBody OrderRequest orderRequest) {
        orderService.update(orderId, orderRequest);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Item has updated")
                .build());
    }

    @GetMapping("{orderId}")
    ResponseEntity<GlobalResponse<Object>> get(@PathVariable("orderId") String orderId) {
        OrderResponse orderResponse = orderService.get(orderId);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .result(orderResponse)
                .build());
    }
}
