package com.cmcabrera.cardcostapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request DTO for calculating card cost")
public class CardCostRequestDTO {
    @NotNull
    @Size(min = 8, max = 19)
    @JsonProperty("card_number")
    @Schema(description = "Payment card number (BIN)", example = "4571736000000000", minLength = 8, maxLength = 19)
    private String cardNumber;
}
