package com.obs.recruitment.order.item.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.recruitment.order.item.inventory.dto.request.InventoryRequest;
import com.obs.recruitment.order.item.inventory.dto.response.InventoryResponse;
import com.obs.recruitment.order.item.inventory.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @BeforeEach
    public void setup() {
        when(inventoryService.get(any(Integer.class))).thenReturn(new InventoryResponse());
        when(inventoryService.getAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
    }

    @Test
    void delete() throws Exception {
        Integer itemId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/inventory/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has deleted"));

        verify(inventoryService, times(1)).delete(itemId);
    }

    @Test
    void getAll() throws Exception {
        int page = 0;
        int size = 5;

        mockMvc.perform(MockMvcRequestBuilders.get("/inventory?page={page}&size={size}", page, size))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content").isArray());

        verify(inventoryService, times(1)).getAll(PageRequest.of(page, size));
    }

    @Test
    void create() throws Exception {
        InventoryRequest request = new InventoryRequest();

        doNothing().when(inventoryService).create(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has been created"));

    }

    @Test
    void update() throws Exception {
        Integer itemId = 1;
        InventoryRequest request = new InventoryRequest();
        doNothing().when(inventoryService).update(anyInt(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/inventory/{id}", itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has updated"));

    }

    @Test
    void get() throws Exception {
        Integer itemId = 1;

        mockMvc.perform(MockMvcRequestBuilders.get("/inventory/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists());

        verify(inventoryService, times(1)).get(itemId);
    }

}