package com.supermarketims.model;

import jakarta.persistence.*;

@Entity
@Table(name = "items")  // Explicitly map to "items" table
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Ensure name cannot be null
    private String name;

    @Column(nullable = false)
    private String category;  // Added category field to match the DB table

    @Column(nullable = false)
    private double price;

    @Column(name = "stock_quantity", nullable = false)  // Match DB column name
    private int stockQuantity;

    // Default constructor (required by JPA)
    public Item() {
    }

    // Constructor with all fields
    public Item(String name, String category, double price, int stockQuantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
