package com.transaction.service;

import com.transaction.dto.UnitDTO;
import com.transaction.dto.mapper.UnitMapper;
import com.transaction.entity.Unit;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public UnitDTO create(UnitDTO unitDTO) {
        log.debug("Saving unit: {}", unitDTO.name());
        Unit unit = unitMapper.toEntity(unitDTO);
        Unit saved = unitRepository.save(unit);
        return unitMapper.toDto(saved);
    }

    public List<UnitDTO> findAll() {
        log.debug("Fetching all units");
        return unitRepository.findAll()
                .stream()
                .map(unitMapper::toDto)
                .collect(Collectors.toList());
    }

    public UnitDTO findById(Long id) {
        log.debug("Fetching unit with ID: {}", id);
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Unit not found with id: " + id));
        return unitMapper.toDto(unit);
    }

    public UnitDTO update(Long id, UnitDTO updatedDTO) {
        log.debug("Updating unit with ID: {}", id);
        Unit existing = unitRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Unit not found with id: " + id));

        existing.setName(updatedDTO.name());
        // Add setters for other fields if your DTO contains them

        Unit saved = unitRepository.save(existing);
        return unitMapper.toDto(saved);
    }

    public void delete(Long id) {
        log.debug("Deleting unit with ID: {}", id);
        if (!unitRepository.existsById(id)) {
            throw new BadRequestException("Unit not found with id: " + id);
        }
        unitRepository.deleteById(id);
    }
}
