package com.supermarketims.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders") // Fix table name
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // Use ENUM for status

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items; // Fix items mapping

    public Order() {}

    public Order(OrderStatus status, List<OrderItem> items) {
        this.status = status;
        this.items = items;
    }

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

    public List<OrderItem> getItems() { // FIX getItems()
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
