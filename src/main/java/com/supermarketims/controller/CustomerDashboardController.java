package com.supermarketims.controller;

import com.supermarketims.model.*;
import com.supermarketims.repository.InventoryRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@Controller
@RequestMapping("/customer")
public class CustomerDashboardController {

     @Autowired
    private InventoryRepository inventoryRepository;


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

    @GetMapping("/orders")
    public String showCustomerOrders() {
        return "customer-orders"; // customer-orders.html
    }
}
