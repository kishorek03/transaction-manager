package com.transaction.service;

import com.transaction.dto.CashMovementDTO;
import com.transaction.dto.mapper.CashMovementMapper;
import com.transaction.entity.CashMovement;
import com.transaction.repository.CashMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public interface CashMovementService {
    CashMovementDTO createMovement(CashMovementDTO dto);
    List<CashMovementDTO> getAllMovements();

    BigDecimal getNetCashMovementByDate(LocalDate date);

    @Service
    @RequiredArgsConstructor
    class Impl implements CashMovementService {

        private final CashMovementRepository repository;
        private final CashMovementMapper mapper;

        @Override
        public BigDecimal getNetCashMovementByDate(LocalDate date) {
            return repository
                    .getNetCashMovementByDate(date)
                    .orElse(BigDecimal.ZERO);
        }

        @Override
        public CashMovementDTO createMovement(CashMovementDTO dto) {
            CashMovement entity = mapper.toEntity(dto);
            CashMovement saved = repository.save(entity);
            return mapper.toDto(saved);
        }

        @Override
        public List<CashMovementDTO> getAllMovements() {
            return repository.findAll()
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}
