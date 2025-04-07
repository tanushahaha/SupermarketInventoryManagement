package com.supermarketims.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") 
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName; // ✅ Add customerName to match "customer_name" column

    private Double totalPrice; // ✅ Add totalPrice to match "total_price" column

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // ✅ Use ENUM for status

    private LocalDateTime orderDate;
    
    private Long userId; // ✅ Stores the user ID

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items; // ✅ Ensure correct mapping

    public Order(){}

    // ✅ Constructor to initialize order
    public Order(Long userId, String customerName, Double totalPrice, OrderStatus status) {
        this.userId = userId;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = LocalDateTime.now();
    }

    // ✅ Getter and Setter Methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addOrderItem(OrderItem item) { // ✅ Add item to order
        items.add(item);
        item.setOrder(this); // Ensuring bi-directional relationship
    }

    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTotalAmount() { return totalPrice; }
    public void setTotalAmount(Double totalPrice) { this.totalPrice = totalPrice; }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
