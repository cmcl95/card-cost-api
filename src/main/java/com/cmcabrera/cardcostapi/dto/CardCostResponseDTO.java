package com.cmcabrera.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Response DTO for calculated card cost")
public class CardCostResponseDTO {
    @Schema(description = "ISO 2 country code of the card issuing country", example = "US")
    private String country;
    @Schema(description = "Calculated clearing cost in USD", example = "5.00")
    private BigDecimal cost;
}
