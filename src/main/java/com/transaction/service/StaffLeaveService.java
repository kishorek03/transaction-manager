package com.transaction.service;

import com.transaction.dto.StaffLeaveDTO;
import com.transaction.dto.mapper.StaffLeaveMapper;
import com.transaction.entity.StaffLeave;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.StaffLeaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffLeaveService {

    private final StaffLeaveRepository staffLeaveRepository;
    private final StaffLeaveMapper staffLeaveMapper;

    @Transactional
    public StaffLeaveDTO recordLeave(StaffLeaveDTO dto) {
        if (dto.staffName() == null || dto.staffName().trim().isEmpty()) {
            throw new BadRequestException("Staff name is required");
        }
        if (dto.leaveDate() == null) {
            throw new BadRequestException("Leave date is required");
        }

        StaffLeave entity = staffLeaveMapper.toEntity(dto);
        StaffLeave saved = staffLeaveRepository.save(entity);
        log.info("Recorded leave for staff '{}' on {}", saved.getStaffName(), saved.getLeaveDate());
        return staffLeaveMapper.toDto(saved);
    }

    public List<StaffLeaveDTO> getLeaves(LocalDate from, LocalDate to, String staffName, String month) {
        LocalDate startDate = from;
        LocalDate endDate = to;

        if (month != null && !month.trim().isEmpty()) {
            try {
                YearMonth ym = YearMonth.parse(month.trim());
                startDate = ym.atDay(1);
                endDate = ym.atEndOfMonth();
            } catch (Exception e) {
                log.warn("Invalid month format '{}', expected YYYY-MM", month);
            }
        }

        List<StaffLeave> leaves;

        if (startDate != null && endDate != null) {
            if (staffName != null && !staffName.trim().isEmpty()) {
                leaves = staffLeaveRepository.findByStaffNameIgnoreCaseAndLeaveDateBetweenOrderByLeaveDateDesc(
                        staffName.trim(), startDate, endDate
                );
            } else {
                leaves = staffLeaveRepository.findByLeaveDateBetweenOrderByLeaveDateDesc(startDate, endDate);
            }
        } else if (staffName != null && !staffName.trim().isEmpty()) {
            leaves = staffLeaveRepository.findAllByOrderByLeaveDateDesc().stream()
                    .filter(l -> l.getStaffName().equalsIgnoreCase(staffName.trim()))
                    .toList();
        } else {
            leaves = staffLeaveRepository.findAllByOrderByLeaveDateDesc();
        }

        return leaves.stream()
                .map(staffLeaveMapper::toDto)
                .toList();
    }

    @Transactional
    public StaffLeaveDTO updateLeave(Long id, StaffLeaveDTO dto) {
        StaffLeave existing = staffLeaveRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Leave record not found with id: " + id));

        if (dto.staffName() != null && !dto.staffName().isBlank()) {
            existing.setStaffName(dto.staffName().trim());
        }
        if (dto.leaveDate() != null) {
            existing.setLeaveDate(dto.leaveDate());
        }
        if (dto.reason() != null) {
            existing.setReason(dto.reason().trim());
        }

        StaffLeave updated = staffLeaveRepository.save(existing);
        log.info("Updated leave record with id: {}", id);
        return staffLeaveMapper.toDto(updated);
    }

    @Transactional
    public void deleteLeave(Long id) {
        if (!staffLeaveRepository.existsById(id)) {
            throw new BadRequestException("Leave record not found with id: " + id);
        }
        staffLeaveRepository.deleteById(id);
        log.info("Deleted leave record with id: {}", id);
    }
}
