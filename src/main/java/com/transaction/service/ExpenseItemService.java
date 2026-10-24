package com.transaction.service;

import com.transaction.dto.ExpenseItemDTO;
import com.transaction.dto.mapper.ExpenseItemMapper;
import com.transaction.entity.Category;
import com.transaction.entity.ExpenseItem;
import com.transaction.entity.Unit;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.CategoryRepository;
import com.transaction.repository.ExpenseItemRepository;
import com.transaction.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseItemService {

    private final ExpenseItemRepository expenseItemRepository;
    private final CategoryRepository categoryRepository;
    private final UnitRepository unitRepository;
    private final ExpenseItemMapper expenseItemMapper;

    public List<ExpenseItemDTO> getAllExpenseItems(Boolean activeOnly, Long categoryId, String search) {
        boolean filterActive = (activeOnly == null || activeOnly);
        String searchPattern = (search != null && !search.isBlank()) ? search.trim() : null;
        List<ExpenseItem> items = expenseItemRepository.findByFilters(filterActive, categoryId, searchPattern);
        return items.stream()
                .map(expenseItemMapper::toDto)
                .toList();
    }

    public ExpenseItemDTO getExpenseItemById(Long id) {
        ExpenseItem item = expenseItemRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Expense item not found with id: " + id));
        return expenseItemMapper.toDto(item);
    }

    @Transactional
    public ExpenseItemDTO createExpenseItem(ExpenseItemDTO dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new BadRequestException("Invalid category ID: " + dto.categoryId()));

        Unit defaultUnit = null;
        if (dto.defaultUnitId() != null) {
            defaultUnit = unitRepository.findById(dto.defaultUnitId())
                    .orElseThrow(() -> new BadRequestException("Invalid unit ID: " + dto.defaultUnitId()));
        }

        ExpenseItem item = expenseItemMapper.toEntity(dto);
        item.setCategory(category);
        item.setDefaultUnit(defaultUnit);
        if (item.getIsActive() == null) {
            item.setIsActive(true);
        }

        ExpenseItem saved = expenseItemRepository.save(item);
        log.info("Created expense item: {} (ID: {})", saved.getName(), saved.getId());
        return expenseItemMapper.toDto(saved);
    }

    @Transactional
    public ExpenseItemDTO updateExpenseItem(Long id, ExpenseItemDTO dto) {
        ExpenseItem existing = expenseItemRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Expense item not found with id: " + id));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new BadRequestException("Invalid category ID: " + dto.categoryId()));

        Unit defaultUnit = null;
        if (dto.defaultUnitId() != null) {
            defaultUnit = unitRepository.findById(dto.defaultUnitId())
                    .orElseThrow(() -> new BadRequestException("Invalid unit ID: " + dto.defaultUnitId()));
        }

        existing.setName(dto.name());
        existing.setTamilName(dto.tamilName());
        existing.setCategory(category);
        existing.setDefaultUnit(defaultUnit);
        existing.setDefaultPrice(dto.defaultPrice());
        if (dto.isActive() != null) {
            existing.setIsActive(dto.isActive());
        }

        ExpenseItem updated = expenseItemRepository.save(existing);
        log.info("Updated expense item ID {}: {}", id, updated.getName());
        return expenseItemMapper.toDto(updated);
    }

    @Transactional
    public void deleteExpenseItem(Long id) {
        ExpenseItem existing = expenseItemRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Expense item not found with id: " + id));
        // Soft delete
        existing.setIsActive(false);
        expenseItemRepository.save(existing);
        log.info("Soft deleted expense item ID {}", id);
    }
}
