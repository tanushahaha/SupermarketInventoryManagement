package com.supermarketims.controller;

import com.supermarketims.model.*;
import com.supermarketims.repository.OrderItemRepository;
import com.supermarketims.repository.OrderRepository;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null || loggedInUser.getRole() != UserRole.ADMIN) {
            return "redirect:/user/login";
        }
        model.addAttribute("username", loggedInUser.getUsername());
        return "admin-dashboard"; // admin-dashboard.html
    }

    @GetMapping("/inventory")
    public String showInventory() {
        return "inventory"; // inventory.html
    }

    @GetMapping("/checkout/history")
    public String getCheckoutHistory(Model model) {
        List<Order> orders = orderRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
    
        for (Order order : orders) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", order.getId());
            row.put("customerName", order.getCustomerName());
            row.put("totalAmount", order.getTotalAmount());
            row.put("date", order.getOrderDate());
    
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
    
            List<String> items = orderItems.stream()
                    .map(oi -> oi.getItem().getName() + " (x" + oi.getQuantity() + ")")
                    .collect(Collectors.toList());
    
            row.put("items", items);
            result.add(row);
        }
    
        model.addAttribute("checkoutHistory", result);
        return "checkout"; // This should be in src/main/resources/templates/checkout.html
    }
    




    @GetMapping("/stock_orders")
    public String showOrders() {
        return "stock_orders"; // orders.html
    }
}
