package com.obs.recruitment.order.item.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obs.recruitment.order.item.item.dto.request.ItemRequest;
import com.obs.recruitment.order.item.item.dto.response.ItemResponse;
import com.obs.recruitment.order.item.item.service.ItemService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    void getAllItems() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        List<ItemResponse> itemResponses = easyRandom.objects(ItemResponse.class, 1)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList));
        Page<ItemResponse> itemResponsePage = new PageImpl<>(itemResponses);
        when(itemService.getAll(any(), anyString())).thenReturn(itemResponsePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/item")
                        .param("page", "0")
                        .param("size", "5")
                        .param("search", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists())
                .andDo(print());
    }

    @Test
    void getItemById() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        int itemId = 1;

        ItemResponse itemResponse = easyRandom.nextObject(ItemResponse.class);

        when(itemService.getResponse(anyInt())).thenReturn(itemResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/item/{id}", itemId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").exists())
                .andDo(print());
    }

    @Test
    void createItem() throws Exception {
        ItemRequest request = new ItemRequest();
        request.setName("Test Item");

        ObjectMapper objectMapper = new ObjectMapper();

        doNothing().when(itemService).create(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has been created"))
                .andDo(print());
    }

    @Test
    public void updateItem() throws Exception {
        int existingItemId = 1; // Assuming this item ID exists

        ObjectMapper objectMapper = new ObjectMapper();

        ItemRequest request = new ItemRequest();
        request.setName("Updated Item Name");
        doNothing().when(itemService).update(anyInt(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/item/{id}", existingItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has updated"))
                .andDo(print());
    }

    @Test
    public void deleteItem() throws Exception {
        int existingItemId = 1; // Assuming this item ID exists

        mockMvc.perform(MockMvcRequestBuilders.delete("/item/{id}", existingItemId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item has deleted"))
                .andDo(print());
    }
}