package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.PaymentMethodDTO;
import com.transaction.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @GetMapping
    public ApiResponse<List<PaymentMethodDTO>> getAllMethods() {
        log.info("Fetching all payment methods");
        List<PaymentMethodDTO> methods = paymentMethodService.getAllMethods();
        return new ApiResponse<>("success", "Payment methods retrieved", methods);
    }

    @PostMapping
    public ApiResponse<PaymentMethodDTO> createMethod(@RequestBody PaymentMethodDTO methodDTO) {
        log.info("Creating new payment method: {}", methodDTO.name());
        PaymentMethodDTO responseDTO = paymentMethodService.createMethod(methodDTO);
        return new ApiResponse<>("success", "Payment method created successfully", responseDTO);
    }

    @PutMapping("/{id}")
    public ApiResponse<PaymentMethodDTO> updateMethod(@PathVariable Long id, @RequestBody PaymentMethodDTO methodDTO) {
        log.info("Updating payment method with ID: {}", id);
        PaymentMethodDTO responseDTO = paymentMethodService.updateMethod(id, methodDTO);
        return new ApiResponse<>("success", "Payment method updated successfully", responseDTO);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMethod(@PathVariable Long id) {
        log.info("Deleting payment method with ID: {}", id);
        paymentMethodService.deleteMethod(id);
        return new ApiResponse<>("success", "Payment method deleted successfully", null);
    }
}
