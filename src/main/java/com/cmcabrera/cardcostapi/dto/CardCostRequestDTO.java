package com.cmcabrera.cardcostapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardCostRequestDTO {
    @NotNull
    @Size(min = 8, max = 19)
    @JsonProperty("card_number")
    private String cardNumber;
}
