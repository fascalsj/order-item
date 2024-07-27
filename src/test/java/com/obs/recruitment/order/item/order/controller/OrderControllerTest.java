package com.obs.recruitment.order.item.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.recruitment.order.item.order.dto.request.OrderRequest;
import com.obs.recruitment.order.item.order.dto.response.OrderResponse;
import com.obs.recruitment.order.item.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Optional setup before each test
    }

    @Test
    void testDeleteOrder() throws Exception {
        String orderId = "123";
        mockMvc.perform(MockMvcRequestBuilders.delete("/order/{orderId}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has deleted"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        Page<OrderResponse> page = Page.empty(); // Example empty page response
        when(orderService.getAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/order?page=0&size=5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists());
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        String requestJson = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has been created"));
    }

    @Test
    void testUpdateOrder() throws Exception {
        String orderId = "123";
        OrderRequest orderRequest = new OrderRequest();
        String requestJson = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/order/{orderId}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has updated"));
    }

    @Test
    void testGetOrder() throws Exception {
        String orderId = "123";
        OrderResponse orderResponse = new OrderResponse();
        when(orderService.get(orderId)).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/order/{orderId}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists());
    }
}