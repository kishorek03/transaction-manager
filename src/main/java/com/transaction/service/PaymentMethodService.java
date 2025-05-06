package com.transaction.service;

import com.transaction.dto.PaymentMethodDTO;
import com.transaction.dto.mapper.PaymentMethodMapper;
import com.transaction.entity.PaymentMethod;
import com.transaction.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    // Fetch all payment methods
    public List<PaymentMethodDTO> getAllMethods() {
        List<PaymentMethod> methods = paymentMethodRepository.findAll();
        return methods.stream()
                .map(paymentMethodMapper::toDto)
                .collect(Collectors.toList());
    }

    // Create a new payment method
    public PaymentMethodDTO createMethod(PaymentMethodDTO methodDTO) {
        PaymentMethod method = paymentMethodMapper.toEntity(methodDTO);
        PaymentMethod savedMethod = paymentMethodRepository.save(method);
        return paymentMethodMapper.toDto(savedMethod);
    }

    // Update an existing payment method
    public PaymentMethodDTO updateMethod(Long id, PaymentMethodDTO methodDTO) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));
        method.setName(methodDTO.name());
        PaymentMethod updatedMethod = paymentMethodRepository.save(method);
        return paymentMethodMapper.toDto(updatedMethod);
    }

    // Delete a payment method by ID
    public void deleteMethod(Long id) {
        paymentMethodRepository.deleteById(id);
    }
}
