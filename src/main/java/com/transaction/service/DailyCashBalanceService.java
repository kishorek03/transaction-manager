package com.transaction.service;

import com.transaction.dto.DailyCashBalanceDTO;
import com.transaction.dto.mapper.DailyCashBalanceMapper;
import com.transaction.entity.DailyCashBalance;
import com.transaction.repository.DailyCashBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyCashBalanceService {

    DailyCashBalanceDTO createOpeningBalance(DailyCashBalanceDTO dto);

    Optional<DailyCashBalanceDTO> getBalanceByDate(LocalDate date);

    @Service
    @RequiredArgsConstructor
    class DailyCashBalanceServiceImpl implements DailyCashBalanceService {

        private final DailyCashBalanceRepository repository;
        private final DailyCashBalanceMapper mapper;

        @Override
        public DailyCashBalanceDTO createOpeningBalance(DailyCashBalanceDTO dto) {
            repository.findByBusinessDate(dto.businessDate()).ifPresent(existing -> {
                throw new IllegalStateException("Opening balance for this date already exists");
            });

            DailyCashBalance entity = mapper.toEntity(dto);
            DailyCashBalance saved = repository.save(entity);
            return mapper.toDto(saved);
        }

        @Override
        public Optional<DailyCashBalanceDTO> getBalanceByDate(LocalDate date) {
            return repository.findByBusinessDate(date)
                    .map(mapper::toDto);
        }
    }
}
