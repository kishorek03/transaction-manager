package com.transaction.service;

import com.transaction.entity.Unit;
import com.transaction.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    public Optional<Unit> findById(Long id) {
        return unitRepository.findById(id);
    }

    public Unit save(Unit unit) {
        return unitRepository.save(unit);
    }

    public void deleteById(Long id) {
        unitRepository.deleteById(id);
    }
}
