package com.transaction.controller;

import com.transaction.entity.PaymentMethod;
import com.transaction.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @GetMapping
    public List<PaymentMethod> getAllMethods() {
        return paymentMethodRepository.findAll();
    }

    @PostMapping
    public PaymentMethod createMethod(@RequestBody PaymentMethod method) {
        return paymentMethodRepository.save(method);
    }

    @PutMapping("/{id}")
    public PaymentMethod updateMethod(@PathVariable Long id, @RequestBody PaymentMethod details) {
        PaymentMethod method = paymentMethodRepository.findById(id).orElseThrow();
        method.setName(details.getName());
        return paymentMethodRepository.save(method);
    }

    @DeleteMapping("/{id}")
    public void deleteMethod(@PathVariable Long id) {
        paymentMethodRepository.deleteById(id);
    }
}
