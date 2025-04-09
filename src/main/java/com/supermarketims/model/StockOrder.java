package com.supermarketims.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "stock_orders")
public class StockOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockOrderStatus status;

    @OneToMany(mappedBy = "stockOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockOrderItem> items;

    public StockOrder() {}

    public StockOrder(StockOrderStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockOrderStatus getStatus() {
        return status;
    }

    public void setStatus(StockOrderStatus status) {
        this.status = status;
    }

    public List<StockOrderItem> getItems() {
        return items;
    }

    public void setItems(List<StockOrderItem> items) {
        this.items = items;
    }
}
