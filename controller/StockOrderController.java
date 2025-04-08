package com.supermarketims.controller;

import java.util.List;

import com.supermarketims.model.StockOrder;
import com.supermarketims.repository.StockOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockOrderController {

    @Autowired
    private StockOrderRepository stockOrderRepository;
    
    @GetMapping("/orders")
    public List<StockOrder> getAllStockOrders() {
    return stockOrderRepository.findAll();
}

    @PostMapping("/place")
    public ResponseEntity<String> placeStockOrder(@RequestBody StockOrder order) {
        stockOrderRepository.save(order);
        return ResponseEntity.ok("Stock order placed successfully");
    }
}
