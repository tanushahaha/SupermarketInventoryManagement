package com.supermarketims.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermarketims.model.*;
import com.supermarketims.repository.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;





@Controller
@RequestMapping("/customer")
public class CustomerDashboardController {

     @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/dashboard")
    public String showCustomerDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null || loggedInUser.getRole() != UserRole.CUSTOMER) {
            return "redirect:/user/login";
        }
        model.addAttribute("username", loggedInUser.getUsername());
        List<Item> availableItems = inventoryRepository.findAll();
        model.addAttribute("items", availableItems);
        return "customer-dashboard"; // customer-dashboard.html
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            model.addAttribute("error", "User not logged in!");
            return "redirect:/user/login";
        }
    
        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        double totalAmount = cartItems.stream()
        .mapToDouble(c -> c.getItem().getPrice() * c.getQuantity())
        .sum();

    model.addAttribute("cartItems", cartItems);
    model.addAttribute("totalAmount", totalAmount);
    
        return "customer-checkout"; // must match the HTML file name
    }

    @PostMapping("/checkout")
    public String handleCheckout(@RequestParam("cartJson") String cartJson, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
    
        if (user == null) {
            model.addAttribute("error", "User not logged in!");
            return "redirect:/user/login";
        }
    
        // Parse JSON cart string to Java List
        ObjectMapper objectMapper = new ObjectMapper();
        List<CartItem> cartItems;
    
        try {
            cartItems = objectMapper.readValue(cartJson, new TypeReference<List<CartItem>>() {});
        } catch (IOException e) {
            model.addAttribute("error", "Invalid cart data!");
            return "customer-checkout";
        }
        
    
        for (CartItem item : cartItems) {
            Item dbItem = inventoryRepository.findById(item.getItemId()).orElse(null);
            if (dbItem == null || dbItem.getStockQuantity() < item.getQuantity()) {
                model.addAttribute("error", "Insufficient stock for item: " + item.getItemId());
                return "customer-checkout";
            }
        
            // Save to cart table
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setItem(dbItem);
            cart.setQuantity(item.getQuantity());
            cartRepository.save(cart);
        
            // Decrease item stock
            dbItem.setStockQuantity(dbItem.getStockQuantity() - item.getQuantity());
            inventoryRepository.save(dbItem);
        }
    
        return "redirect:/customer/checkout";
    }


}
