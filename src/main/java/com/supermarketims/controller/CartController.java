package com.supermarketims.controller;

import com.supermarketims.model.Cart;
import com.supermarketims.model.Item;
import com.supermarketims.model.User;
import com.supermarketims.repository.CartRepository;
import com.supermarketims.repository.InventoryRepository;
import com.supermarketims.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    static class CartRequest {
        public Long id;
        public int quantity;
    }
    
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody List<CartRequest> cartItems, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("User not logged in");
        }

        for (CartRequest cartRequest : cartItems) {
            Optional<Item> item = inventoryRepository.findById(cartRequest.id);
            if (item.isPresent()) {
                Cart cart = new Cart(user, item.get(), cartRequest.quantity);
                cartRepository.save(cart);
            }
        }
        return ResponseEntity.ok("Items added to cart");
    }
    
    @GetMapping("/view")
    public ResponseEntity<List<Cart>> viewCart(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        return ResponseEntity.ok(cartItems);
    }
    
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody List<CartRequest> cartItems, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.badRequest().body("User not logged in");
        }

        for (CartRequest cartRequest : cartItems) {
            Optional<Item> inventoryItem = inventoryRepository.findById(cartRequest.id);
            if (inventoryItem.isPresent() && inventoryItem.get().getStockQuantity() >= cartRequest.quantity) {
                Item updatedItem = inventoryItem.get();
                updatedItem.setStockQuantity(updatedItem.getStockQuantity() - cartRequest.quantity);
                inventoryRepository.save(updatedItem);
            } else {
                return ResponseEntity.badRequest().body("Insufficient stock for item: " + inventoryItem.get().getName());
            }
        }

        cartRepository.deleteByUserId(user.getId());
        return ResponseEntity.ok("Checkout successful");
    }
}
