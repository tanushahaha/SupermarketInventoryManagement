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
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;





@Controller
@RequestMapping("/customer")
public class CustomerDashboardController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/dashboard")
    public String showCustomerDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null || loggedInUser.getRole() != UserRole.CUSTOMER) {
            return "redirect:/user/login";
        }
        session.setAttribute("userId", loggedInUser.getId());
        session.setAttribute("username", loggedInUser.getUsername());
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
    double totalBeforeDiscount = 0.0;
    double finalDiscountedTotal = 0.0;
    LocalDateTime now = LocalDateTime.now();

    List<CartItem> checkoutCartItems = new ArrayList<>();

    for (Cart cartItem : cartItems) {
        Item dbItem = cartItem.getItem();
        double originalPrice = dbItem.getPrice();
        double discountedPrice = originalPrice;
        double discountPercent = 0;

        List<Discount> activeDiscounts = discountRepository
            .findByItemIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                dbItem.getId(),
                now.toLocalDate(),
                now.toLocalDate()
            );

        if (!activeDiscounts.isEmpty()) {
            discountPercent = activeDiscounts.get(0).getDiscountPercentage();
            discountedPrice = originalPrice - (originalPrice * discountPercent / 100);
        }

        // Accumulate totals
        totalBeforeDiscount += originalPrice * cartItem.getQuantity();
        finalDiscountedTotal += discountedPrice * cartItem.getQuantity();

        // Create and add custom CartItem for display
        CartItem checkoutItem = new CartItem();
        checkoutItem.setItemId(dbItem.getId());
        checkoutItem.setName(dbItem.getName());
        checkoutItem.setQuantity(cartItem.getQuantity());
        checkoutItem.setPrice(originalPrice);
        checkoutItem.setDiscount(discountPercent);

        checkoutCartItems.add(checkoutItem);
    }

    model.addAttribute("totalBeforeDiscount", totalBeforeDiscount);
    model.addAttribute("finalDiscountedTotal", finalDiscountedTotal);
    model.addAttribute("cartDetails", checkoutCartItems); // ✅ Added for detailed display

    return "customer-checkout";
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
    @PostMapping("/confirm-payment")
    public String confirmPayment(@RequestParam("paymentMethod") String paymentMethod,
                             @RequestParam(value = "cardNumber", required = false) String cardNumber,
                             @RequestParam(value = "expiryDate", required = false) String expiryDate,
                             @RequestParam(value = "cvv", required = false) String cvv,
                             HttpSession session,
                             Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login"; // or any login page
        }
        Long userId = user.getId();
        String customerName = (String) session.getAttribute("username");

        List<Cart> cartItems = cartRepository.findByUserId(userId);


        if (cartItems.isEmpty()) {
            model.addAttribute("message", "Cart is empty.");
            return "redirect:/customer/dashboard";
        }

        double total = 0.0;
    LocalDateTime now = LocalDateTime.now();

    for (Cart cartItem : cartItems) {
        Item item = cartItem.getItem();
        double originalPrice = item.getPrice();
        double discountedPrice = originalPrice;

        // Get active discount (if any)
        List<Discount> activeDiscounts = discountRepository
            .findByItemIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                item.getId(),
                now.toLocalDate(),
                now.toLocalDate()
            );

        if (!activeDiscounts.isEmpty()) {
            double discountPercent = activeDiscounts.get(0).getDiscountPercentage();
            discountedPrice = originalPrice - (originalPrice * discountPercent / 100);
        }

        total += discountedPrice * cartItem.getQuantity();
    }


            // 1. Save Order
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setTotalAmount(total);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.COMPLETED);
            order.setUserId(userId);
            order = orderRepository.save(order);

            for (Cart cartItem : cartItems) {
                Item item = cartItem.getItem();
                double originalPrice = item.getPrice();
                double discountedPrice = originalPrice;
            
                List<Discount> activeDiscounts = discountRepository
                    .findByItemIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        item.getId(),
                        now.toLocalDate(),
                        now.toLocalDate()
                    );
            
                if (!activeDiscounts.isEmpty()) {
                    double discountPercent = activeDiscounts.get(0).getDiscountPercentage();
                    discountedPrice = originalPrice - (originalPrice * discountPercent / 100);
                }
            
                OrderItem orderItem = new OrderItem();
                OrderItemId orderItemId = new OrderItemId();
                orderItemId.setOrderId(order.getId());
                orderItemId.setItemId(item.getId());
            
                orderItem.setId(orderItemId);
                orderItem.setOrder(order);
                orderItem.setItem(item);
                orderItem.setQuantity(cartItem.getQuantity());

                // Optional: Save price per unit (if your entity has a `price` field)
                // orderItem.setPricePerUnit(discountedPrice); 
            
                orderItemRepository.save(orderItem);
            }


            // 2. Save Payment
            Payment payment = new Payment();
            payment.setAmount(total);
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(paymentMethod);
            payment.setOrder(order);
            paymentRepository.save(payment);

            // 3. Clear Cart
            cartRepository.deleteByUserId(userId);

            model.addAttribute("message", "✅ Payment successful!");
            model.addAttribute("order", order);
            session.setAttribute("user", user); // restore user in session (if lost)

            return "customer-payment-success";

        }



}
