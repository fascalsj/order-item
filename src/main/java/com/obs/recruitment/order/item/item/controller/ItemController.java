package com.obs.recruitment.order.item.item.controller;

import com.obs.recruitment.order.item.item.dto.request.ItemRequest;
import com.obs.recruitment.order.item.item.dto.response.ItemResponse;
import com.obs.recruitment.order.item.item.service.ItemService;
import com.obs.recruitment.order.item.shared.dto.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @DeleteMapping("{id}")
    ResponseEntity<GlobalResponse<Object>> delete(@PathVariable("id") Integer id) {
        itemService.delete(id);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Item has deleted")
                .build());
    }

    @GetMapping()
    ResponseEntity<GlobalResponse<Object>> getAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "5") int size,
                                                  @RequestParam(name = "search", defaultValue = "") String search) {
        Page<ItemResponse> itemResponses = itemService.getAll(PageRequest.of(page, size), search);
        return ResponseEntity.ok().body(
                GlobalResponse.builder()
                        .code(HttpStatus.OK.value())
                        .result(itemResponses)
                        .build()
        );
    }

    @PostMapping()
    ResponseEntity<GlobalResponse<Object>> create(@RequestBody ItemRequest itemRequest) {
        itemService.create(itemRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(GlobalResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Item has been created")
                .build());
    }

    @PutMapping("{id}")
    ResponseEntity<GlobalResponse<Object>> update(@PathVariable("id") Integer id,
                                                  @RequestBody ItemRequest itemRequest) {
        itemService.update(id, itemRequest);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Item has updated")
                .build());
    }

    @GetMapping("{id}")
    ResponseEntity<GlobalResponse<Object>> get(@PathVariable("id") Integer id) {
        ItemResponse itemResponse = itemService.getResponse(id);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .result(itemResponse)
                .build());
    }
}
