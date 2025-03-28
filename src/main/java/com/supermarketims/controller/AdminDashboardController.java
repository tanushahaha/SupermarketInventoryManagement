package com.supermarketims.controller;

import com.supermarketims.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

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

    @GetMapping("/checkout")
    public String showCheckout() {
        return "checkout"; // orders.html
    }

    @GetMapping("/stock_orders")
    public String showOrders() {
        return "stock_orders"; // orders.html
    }
}
