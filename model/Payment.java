package com.supermarketims.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private String paymentMethod; // e.g., "Credit Card", "Cash", "UPI"

    @Column(nullable = false, updatable = false)
    private LocalDateTime paymentDate; // Auto-filled on payment creation

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Linking payment to an order

    // ✅ Constructors
    public Payment() {}

    public Payment(Order order, String paymentMethod, double amount) {
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = PaymentStatus.PENDING; // Default status on creation
        this.paymentDate = LocalDateTime.now(); // Auto-set when created
    }
    

    // ✅ Getters & Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public double getAmount() { return amount; }

    public void setAmount(double amount) { this.amount = amount; }

    public PaymentStatus getStatus() { return status; }

    public void setStatus(PaymentStatus status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDateTime getPaymentDate() { return paymentDate; }

    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }

    public Order getOrder() { return order; }

    public void setOrder(Order order) { this.order = order; }
}
