package com.obs.recruitment.inventory.inventory.controller;

import com.obs.recruitment.inventory.inventory.dto.request.InventoryRequest;
import com.obs.recruitment.inventory.inventory.dto.response.InventoryResponse;
import com.obs.recruitment.inventory.inventory.service.InventoryService;
import com.obs.recruitment.inventory.shared.dto.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @DeleteMapping("{id}")
    ResponseEntity<GlobalResponse<Object>> delete(@PathVariable("id") Integer id) {
        inventoryService.delete(id);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Item has deleted")
                .build());
    }

    @GetMapping()
    ResponseEntity<GlobalResponse<Object>> getAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                                  @RequestParam(name = "size", defaultValue = "5") int size) {
        Page<InventoryResponse> itemResponses = inventoryService.getAll(PageRequest.of(page, size));
        return ResponseEntity.ok().body(
                GlobalResponse.builder()
                        .code(HttpStatus.OK.value())
                        .result(itemResponses)
                        .build()
        );
    }

    @PostMapping()
    ResponseEntity<GlobalResponse<Object>> update(@RequestBody InventoryRequest itemRequest) {
        inventoryService.create(itemRequest);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.CREATED.value())
                .message("Item has been created")
                .build());
    }

    @PutMapping("{id}")
    ResponseEntity<GlobalResponse<Object>> update(@PathVariable("id") Integer id,
                                                  @RequestBody InventoryRequest itemRequest) {
        inventoryService.update(id, itemRequest);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Item has updated")
                .build());
    }

    @GetMapping("{id}")
    ResponseEntity<GlobalResponse<Object>> get(@PathVariable("id") Integer id) {
        InventoryResponse itemResponse = inventoryService.get(id);
        return ResponseEntity.ok().body(GlobalResponse.builder()
                .code(HttpStatus.OK.value())
                .result(itemResponse)
                .build());
    }
}
