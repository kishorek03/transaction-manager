package com.transaction.controller;

import com.transaction.entity.Sales;
import com.transaction.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping
    public Sales create(@RequestBody Sales sale) {
        return salesService.save(sale);
    }

    @GetMapping
    public List<Sales> getAll() {
        return salesService.findAll();
    }

    @GetMapping("/{id}")
    public Sales getById(@PathVariable Long id) {
        return salesService.findById(id);
    }

    @PutMapping("/{id}")
    public Sales update(@PathVariable Long id, @RequestBody Sales updatedSale) {
        return salesService.update(id, updatedSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        salesService.delete(id);
        return ResponseEntity.ok().build();
    }
}
