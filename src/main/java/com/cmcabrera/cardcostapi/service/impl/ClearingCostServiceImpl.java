package com.cmcabrera.cardcostapi.service.impl;

import com.cmcabrera.cardcostapi.entity.ClearingCost;
import com.cmcabrera.cardcostapi.repository.ClearingCostRepository;
import com.cmcabrera.cardcostapi.service.ClearingCostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClearingCostServiceImpl implements ClearingCostService {

    private final ClearingCostRepository repository;

    @Override
    public List<ClearingCost> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ClearingCost> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ClearingCost save(ClearingCost clearingCost) {
        return repository.save(clearingCost);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
