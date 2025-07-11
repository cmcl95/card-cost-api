package com.cmcabrera.cardcostapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClearingCostDTO {
    private Long id;

    @NotNull
    @Size(min = 2, max = 2)
    private String countryCode;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal cost;
}
