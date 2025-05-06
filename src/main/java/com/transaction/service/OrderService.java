package com.transaction.service;

import com.transaction.dto.OrderDTO;
import com.transaction.dto.mapper.OrderMapper;
import com.transaction.entity.Order;
import com.transaction.repository.OrderRepository;
import com.transaction.repository.PaymentMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public OrderDTO createOrder(OrderDTO dto) {
        Order order = orderMapper.toEntity(dto);

        order.setPaymentMethod(
            paymentMethodRepository.findById(dto.paymentMethodId())
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found"))
        );

        order.getSales().forEach(sale -> sale.setOrder(order));

        Order saved = orderRepository.save(order);
        return orderMapper.toDTO(saved);
    }

    public List<OrderDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return orderMapper.toDTO(order);
    }
}
