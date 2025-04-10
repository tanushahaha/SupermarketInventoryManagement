package com.supermarketims.model;

public class CartItem {
    private Long itemId;
    private int quantity;
    private String name;
    private double price;
    private double discount; // in percent, like 10 for 10%

    public CartItem() {}

    public CartItem(Long itemId, int quantity, String name, double price, double discount) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.discount = discount;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    // Final price after discount for this item * quantity
    public double getFinalPrice() {
        double discountedUnitPrice = price - (price * discount / 100);
        return discountedUnitPrice * quantity;
    }
}