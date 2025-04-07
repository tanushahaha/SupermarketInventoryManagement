package com.supermarketims.controller;

import com.supermarketims.model.Supplier;
import com.supermarketims.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping("/orders")
    public List<Supplier> getAllSupplierOrders() {
        return supplierRepository.findAll();
    }
}
