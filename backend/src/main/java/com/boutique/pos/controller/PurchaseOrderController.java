package com.boutique.pos.controller;

import com.boutique.pos.dto.CreatePurchaseOrderDTO;
import com.boutique.pos.dto.PurchaseOrderDTO;
import com.boutique.pos.dto.ReceivePurchaseOrderDTO;
import com.boutique.pos.service.PurchaseOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "http://localhost:5173")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }
    @PostMapping
    public ResponseEntity<PurchaseOrderDTO> create(@RequestBody CreatePurchaseOrderDTO dto) {
        PurchaseOrderDTO createdOrder = purchaseOrderService.create(dto);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> getById(@PathVariable Long id) {
        PurchaseOrderDTO order = purchaseOrderService.getById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<PurchaseOrderDTO>> getAll() {
        List<PurchaseOrderDTO> orders = purchaseOrderService.getAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @PatchMapping("/{id}/submit")
    public ResponseEntity<PurchaseOrderDTO> submit(@PathVariable Long id) {
        PurchaseOrderDTO updatedOrder = purchaseOrderService.submit(id);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
    @PatchMapping("/{id}/receive")
    public ResponseEntity<PurchaseOrderDTO> receive(@PathVariable Long id, @RequestBody ReceivePurchaseOrderDTO receiveDto) {
        PurchaseOrderDTO updatedOrder = purchaseOrderService.receive(id, receiveDto);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }
}
