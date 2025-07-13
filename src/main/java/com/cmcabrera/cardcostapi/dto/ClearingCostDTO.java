package com.cmcabrera.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "DTO for Clearing Cost details")
public class ClearingCostDTO {
    @Schema(description = "Unique identifier of the clearing cost", example = "1")
    private Long id;

    @NotNull
    @Size(min = 2, max = 2)
    @Schema(description = "ISO 2 country code", example = "US", minLength = 2, maxLength = 2)
    private String countryCode;

    @NotNull
    @DecimalMin(value = "0.0")
    @Schema(description = "Clearing cost in USD", example = "5.00")
    private BigDecimal cost;
}
