package com.supermarketims.controller;

import com.supermarketims.model.Item;
import com.supermarketims.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private InventoryRepository itemRepository;

    @GetMapping("/products/available")
    @ResponseBody
    public List<Item> getAvailableProducts() {
    return itemRepository.findTop10ByOrderByIdAsc();  
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getProductById(@PathVariable Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Item item) {
        itemRepository.save(item);
        return ResponseEntity.ok("Product added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Item updatedItem) {
        if (itemRepository.existsById(id)) {
            updatedItem.setId(id);
            itemRepository.save(updatedItem);
            return ResponseEntity.ok("Product updated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
