package com.transaction.service;

import java.util.List;
import java.util.stream.Collectors;

import com.transaction.dto.FlavourDTO;
import com.transaction.dto.mapper.FlavourMapper;
import com.transaction.entity.Flavour;
import com.transaction.repository.FlavourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlavourService {

    private final FlavourRepository flavourRepository;
    private final FlavourMapper flavourMapper;

    public FlavourService(FlavourRepository flavourRepository, FlavourMapper flavourMapper) {
        this.flavourRepository = flavourRepository;
        this.flavourMapper = flavourMapper;
    }

    public FlavourDTO create(FlavourDTO dto) {
        Flavour flavour = flavourMapper.toEntity(dto);
        Flavour saved = flavourRepository.save(flavour);
        return flavourMapper.toDto(saved);
    }

    public List<FlavourDTO> getAll() {
        return flavourRepository.findAll()
                .stream()
                .map(flavourMapper::toDto)
                .collect(Collectors.toList());
    }

    public FlavourDTO getById(Long id) {
        Flavour flavour = flavourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flavour not found"));
        return flavourMapper.toDto(flavour);
    }

    @Transactional
    public FlavourDTO update(Long id, FlavourDTO dto) {
        Flavour existing = flavourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flavour not found"));

        existing.setName(dto.name());
        return flavourMapper.toDto(existing); // save not needed due to transactional dirty checking
    }

    public void delete(Long id) {
        flavourRepository.deleteById(id);
    }
}
