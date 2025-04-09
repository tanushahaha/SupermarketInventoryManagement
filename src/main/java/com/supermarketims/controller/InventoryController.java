package com.supermarketims.controller;

import com.supermarketims.model.Item;
import com.supermarketims.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return inventoryRepository.findAll();
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateStock(@RequestBody Item item) {
        Optional<Item> existingItem = inventoryRepository.findById(item.getId());
        if (existingItem.isPresent()) {
            Item updatedItem = existingItem.get();
            updatedItem.setStockQuantity(item.getStockQuantity());
            inventoryRepository.save(updatedItem);
            return ResponseEntity.ok("Stock updated successfully");
        }
        return ResponseEntity.badRequest().body("Item not found");
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestBody Item item) {
        inventoryRepository.save(item);
        return ResponseEntity.ok("Item added successfully");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeItem(@PathVariable Long id) {
        inventoryRepository.deleteById(id);
        return ResponseEntity.ok("Item removed successfully");
    }
}