package com.transaction.service;

import com.transaction.dto.AddOnDTO;
import com.transaction.dto.mapper.AddOnMapper;
import com.transaction.entity.AddOn;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.AddOnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddOnService {

    private final AddOnRepository addOnRepository;
    private final AddOnMapper addOnMapper;

    public AddOnDTO create(AddOnDTO dto) {
        log.debug("Creating AddOn: {}", dto.name());
        AddOn saved = addOnRepository.save(addOnMapper.toEntity(dto));
        return addOnMapper.toDTO(saved);
    }

    public List<AddOnDTO> findAll() {
        log.debug("Retrieving all AddOns");
        return addOnRepository.findAll().stream()
                .map(addOnMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AddOnDTO findById(Long id) {
        log.debug("Fetching AddOn with ID: {}", id);
        AddOn found = addOnRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("AddOn not found with id: " + id));
        return addOnMapper.toDTO(found);
    }

    public AddOnDTO update(Long id, AddOnDTO dto) {
        log.debug("Updating AddOn with ID: {}", id);
        AddOn existing = addOnRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("AddOn not found with id: " + id));

        // Update fields
        existing.setName(dto.name());
        existing.setPrice(dto.price());
        // Add other field mappings as needed

        AddOn updated = addOnRepository.save(existing);
        return addOnMapper.toDTO(updated);
    }

    public void delete(Long id) {
        log.debug("Deleting AddOn with ID: {}", id);
        if (!addOnRepository.existsById(id)) {
            throw new BadRequestException("AddOn not found with id: " + id);
        }
        addOnRepository.deleteById(id);
    }
}
