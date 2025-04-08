package com.supermarketims.controller;

import com.supermarketims.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff")
public class StaffDashboardController {

    @GetMapping("/dashboard")
    public String showStaffDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null || loggedInUser.getRole() != UserRole.STAFF) {
            return "redirect:/user/login";
        }
        model.addAttribute("username", loggedInUser.getUsername());
        return "staff-dashboard"; // staff-dashboard.html
    }

    @GetMapping("/checkout")
    public String showCheckout() {
        return "checkout"; // checkout.html
    }
}
