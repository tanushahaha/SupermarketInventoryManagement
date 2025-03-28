package com.supermarketims.controller;

import com.supermarketims.model.Item;
import com.supermarketims.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private InventoryRepository itemRepository;

    @GetMapping("/suggest/{itemId}")
    public ResponseEntity<List<Item>> suggestAlternatives(@PathVariable Long itemId) {
        List<Item> alternatives = itemRepository.findAlternatives(itemId);
        return ResponseEntity.ok(alternatives);
    }
}
