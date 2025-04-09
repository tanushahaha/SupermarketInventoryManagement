package com.supermarketims.controller;

import com.supermarketims.model.*;
import com.supermarketims.repository.*;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/staff")
public class StaffDashboardController {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierOrderRepository supplierOrderRepository;

    @GetMapping("/dashboard")
    public String viewDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null || loggedInUser.getRole() != UserRole.STAFF) {
            return "redirect:/user/login";
        }
        model.addAttribute("username", loggedInUser.getUsername());
        return "staff-dashboard";
    }

    // 🔹 View all suppliers
    @GetMapping("/suppliers")
    public String viewSuppliers(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        model.addAttribute("newSupplier", new Supplier()); // For the add form
        return "suppliers"; // suppliers.html
    }

    // 🔹 Add a supplier
    @PostMapping("/suppliers/add")
    public String addSupplier(@ModelAttribute Supplier supplier) {
        supplierRepository.save(supplier);
        return "redirect:/staff/suppliers";
    }

    // 🔹 Edit a supplier
    @PostMapping("/suppliers/edit/{id}")
    public String editSupplier(@PathVariable Long id, @ModelAttribute Supplier supplierDetails) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if (supplier != null) {
            supplier.setName(supplierDetails.getName());
            supplier.setContactEmail(supplierDetails.getContactEmail());
            supplier.setPhoneNumber(supplierDetails.getPhoneNumber());
            supplierRepository.save(supplier);
        }
        return "redirect:/staff/suppliers";
    }

    // 🔹 Delete a supplier
    @PostMapping("/suppliers/delete/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        supplierRepository.deleteById(id);
        return "redirect:/staff/suppliers";
    }

    // 🔹 Show order form
    @GetMapping("/order-form")
    public String showOrderForm(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "order-form"; // order-form.html
    }

    // 🔹 Place new stock order
    @PostMapping("/place-order")
    public String placeOrder(@RequestParam Long supplierId,
                             @RequestParam String itemName,
                             @RequestParam int quantity) {
        SupplierOrder order = new SupplierOrder();
        order.setSupplier(supplierRepository.findById(supplierId).orElse(null));
        order.setItemName(itemName);
        order.setQuantity(quantity);
        order.setReceived(false);
        order.setOrderDate(LocalDate.now());
        supplierOrderRepository.save(order);
        return "redirect:/staff/order-form";
    }

    // 🔹 List all supplier orders
    @GetMapping("/suppliers/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", supplierOrderRepository.findAll());
        return "supplier-orders"; // supplier-orders.html
    }
}