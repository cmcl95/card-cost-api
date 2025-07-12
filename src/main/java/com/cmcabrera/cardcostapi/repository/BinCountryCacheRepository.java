package com.cmcabrera.cardcostapi.repository;

import com.cmcabrera.cardcostapi.entity.BinCountryCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinCountryCacheRepository extends JpaRepository<BinCountryCache, String> {
}
