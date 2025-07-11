package com.cmcabrera.cardcostapi.controller;

import com.cmcabrera.cardcostapi.dto.ClearingCostDTO;
import com.cmcabrera.cardcostapi.entity.ClearingCost;
import com.cmcabrera.cardcostapi.mapper.ClearingCostMapper;
import com.cmcabrera.cardcostapi.service.ClearingCostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clearing-costs")
@AllArgsConstructor
public class ClearingCostController {

    private final ClearingCostService clearingCostService;
    private final ClearingCostMapper clearingCostMapper;

    @GetMapping
    public List<ClearingCostDTO> getAllClearingCosts() {
        return clearingCostService.findAll().stream()
                .map(clearingCostMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClearingCostDTO> getClearingCostById(@PathVariable Long id) {
        return clearingCostService.findById(id)
                .map(clearingCostMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClearingCostDTO createClearingCost(@Valid @RequestBody ClearingCostDTO clearingCostDTO) {
        ClearingCost clearingCost = clearingCostMapper.toEntity(clearingCostDTO);
        return clearingCostMapper.toDto(clearingCostService.save(clearingCost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClearingCostDTO> updateClearingCost(@PathVariable Long id, @Valid @RequestBody ClearingCostDTO clearingCostDTO) {
        return clearingCostService.findById(id)
                .map(existingCost -> {
                    existingCost.setCountryCode(clearingCostDTO.getCountryCode());
                    existingCost.setCost(clearingCostDTO.getCost());
                    ClearingCost updatedCost = clearingCostService.save(existingCost);
                    return ResponseEntity.ok(clearingCostMapper.toDto(updatedCost));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClearingCost(@PathVariable Long id) {
        return clearingCostService.findById(id)
                .map(clearingCost -> {
                    clearingCostService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
