package com.supermarketims.controller;

import com.supermarketims.model.*;
import com.supermarketims.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @GetMapping("/history")
    @ResponseBody
    public List<Order> getCheckoutHistory() {
        return orderRepository.findAll();
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody Order order) {
        // Ensure items exist in the order
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("No items in the order");
        }

        for (OrderItem orderItem : order.getItems()) { // ✅ Iterate over List<OrderItem>
            Long itemId = orderItem.getItem().getId();  // Get item ID
            int quantity = orderItem.getQuantity();     // Get quantity
        
            Optional<Item> inventoryItem = inventoryRepository.findById(itemId);
            if (inventoryItem.isPresent()) {
                Item updatedItem = inventoryItem.get();
                if (updatedItem.getStockQuantity() >= quantity) {
                    updatedItem.setStockQuantity(updatedItem.getStockQuantity() - quantity);
                    inventoryRepository.save(updatedItem);
                } else {
                    return ResponseEntity.badRequest().body("Insufficient stock for item ID: " + itemId);
                }
            } else {
                return ResponseEntity.badRequest().body("Item ID not found: " + itemId);
            }
        }
        

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        return ResponseEntity.ok("Checkout successful");
    }
}
