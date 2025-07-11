package com.cmcabrera.cardcostapi.service;

import com.cmcabrera.cardcostapi.entity.ClearingCost;

import java.util.List;
import java.util.Optional;

public interface ClearingCostService {
    List<ClearingCost> findAll();
    Optional<ClearingCost> findById(Long id);
    ClearingCost save(ClearingCost clearingCost);
    void deleteById(Long id);
}
