package com.transaction.controller;

import com.transaction.entity.Unit;
import com.transaction.repository.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    @Autowired
    private UnitRepository unitRepository;

    @GetMapping
    public List<Unit> getAllUnits() {
        return unitRepository.findAll();
    }

    @PostMapping
    public Unit createUnit(@RequestBody Unit unit) {
        return unitRepository.save(unit);
    }

    @PutMapping("/{id}")
    public Unit updateUnit(@PathVariable Long id, @RequestBody Unit details) {
        Unit unit = unitRepository.findById(id).orElseThrow();
        unit.setName(details.getName());
        unit.setAbbreviation(details.getAbbreviation());
        unit.setConversionFactor(details.getConversionFactor());
        unit.setBaseUnit(details.getBaseUnit());
        unit.setUnitType(details.getUnitType());
        return unitRepository.save(unit);
    }

    @DeleteMapping("/{id}")
    public void deleteUnit(@PathVariable Long id) {
        unitRepository.deleteById(id);
    }
}
