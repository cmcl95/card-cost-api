package com.cmcabrera.cardcostapi.mapper;

import com.cmcabrera.cardcostapi.dto.ClearingCostDTO;
import com.cmcabrera.cardcostapi.entity.ClearingCost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClearingCostMapper {
    ClearingCostDTO toDto(ClearingCost clearingCost);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ClearingCost toEntity(ClearingCostDTO clearingCostDTO);
}
