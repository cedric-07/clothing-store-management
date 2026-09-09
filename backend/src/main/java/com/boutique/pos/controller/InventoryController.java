package com.boutique.pos.controller;

import com.boutique.pos.dto.CountInventoryItemDTO;
import com.boutique.pos.dto.CreateInventoryDTO;
import com.boutique.pos.dto.InventoryDTO;
import com.boutique.pos.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "http://localhost:5173")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> create(
            @RequestBody CreateInventoryDTO dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        inventoryService.create(dto)
                );
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAll() {

        return ResponseEntity.ok(
                inventoryService.getAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                inventoryService.getById(id)
        );
    }

    @PatchMapping("/{id}/count")
    public ResponseEntity<InventoryDTO> countItem(
            @PathVariable Long id,
            @RequestBody CountInventoryItemDTO dto
    ) {

        return ResponseEntity.ok(
                inventoryService.countItem(id, dto)
        );
    }

    @PatchMapping("/{id}/validate")
    public ResponseEntity<InventoryDTO> validate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                inventoryService.validate(id)
        );
    }
}
