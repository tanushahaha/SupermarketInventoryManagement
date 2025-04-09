package com.supermarketims.controller;

import com.supermarketims.model.*;
import com.supermarketims.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/refund")
public class RefundController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order updatedOrder = order.get();
            updatedOrder.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(updatedOrder);
            return ResponseEntity.ok("Order cancelled successfully");
        }
        return ResponseEntity.badRequest().body("Order not found");
    }
}
