package com.transaction.service;

import com.transaction.dto.ShopHolidayDTO;
import com.transaction.dto.mapper.ShopHolidayMapper;
import com.transaction.entity.ShopHoliday;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.ShopHolidayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopHolidayService {

    private final ShopHolidayRepository shopHolidayRepository;
    private final ShopHolidayMapper shopHolidayMapper;

    @Transactional
    public ShopHolidayDTO createHoliday(ShopHolidayDTO dto) {
        if (dto.title() == null || dto.title().trim().isEmpty()) {
            throw new BadRequestException("Holiday title is required");
        }
        if (dto.holidayDate() == null) {
            throw new BadRequestException("Holiday date is required");
        }

        ShopHoliday entity = shopHolidayMapper.toEntity(dto);
        if (entity.getIsShopClosed() == null) {
            entity.setIsShopClosed(true);
        }

        ShopHoliday saved = shopHolidayRepository.save(entity);
        log.info("Created shop holiday '{}' on {}", saved.getTitle(), saved.getHolidayDate());
        return shopHolidayMapper.toDto(saved);
    }

    @Transactional
    public ShopHolidayDTO updateHoliday(Long id, ShopHolidayDTO dto) {
        ShopHoliday existing = shopHolidayRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Holiday not found with id: " + id));

        if (dto.title() != null && !dto.title().trim().isEmpty()) {
            existing.setTitle(dto.title().trim());
        }
        if (dto.holidayDate() != null) {
            existing.setHolidayDate(dto.holidayDate());
        }
        if (dto.isShopClosed() != null) {
            existing.setIsShopClosed(dto.isShopClosed());
        }
        if (dto.description() != null) {
            existing.setDescription(dto.description().trim());
        }

        ShopHoliday updated = shopHolidayRepository.save(existing);
        log.info("Updated shop holiday id: {}, title: '{}'", id, updated.getTitle());
        return shopHolidayMapper.toDto(updated);
    }

    public List<ShopHolidayDTO> getHolidays(LocalDate fromDate) {
        List<ShopHoliday> list;
        if (fromDate != null) {
            list = shopHolidayRepository.findByHolidayDateGreaterThanEqualOrderByHolidayDateAsc(fromDate);
        } else {
            list = shopHolidayRepository.findAllByOrderByHolidayDateAsc();
        }
        return list.stream()
                .map(shopHolidayMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteHoliday(Long id) {
        if (!shopHolidayRepository.existsById(id)) {
            throw new BadRequestException("Holiday not found with id: " + id);
        }
        shopHolidayRepository.deleteById(id);
        log.info("Deleted shop holiday id: {}", id);
    }
}
