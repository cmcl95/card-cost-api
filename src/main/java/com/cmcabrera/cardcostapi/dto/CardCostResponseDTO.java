package com.cmcabrera.cardcostapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CardCostResponseDTO {
    private String country;
    private BigDecimal cost;
}
