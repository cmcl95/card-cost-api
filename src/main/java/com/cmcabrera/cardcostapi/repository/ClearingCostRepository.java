package com.cmcabrera.cardcostapi.repository;

import com.cmcabrera.cardcostapi.entity.ClearingCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClearingCostRepository extends JpaRepository<ClearingCost, Long> {
}
