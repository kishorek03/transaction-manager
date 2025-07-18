package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.OrderDTO;
import com.transaction.dto.OrderSummaryDTO;
import com.transaction.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> create(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("Creating Order");
        OrderDTO saved = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Order created", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAll() {
        log.info("Retrieving all orders");
        List<OrderDTO> list = orderService.findAll();
        return ResponseEntity.ok(new ApiResponse<>("success", "Orders retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getById(@PathVariable Long id) {
        log.info("Retrieving Order with ID: {}", id);
        OrderDTO found = orderService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Order found", found));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<List<OrderSummaryDTO>>> getOrderSummaries(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<OrderSummaryDTO> summaries = orderService.getOrderSummariesBetween(start, end);
        return ResponseEntity.ok(new ApiResponse<>("success", "Orders retrieved",summaries));
    }
}
