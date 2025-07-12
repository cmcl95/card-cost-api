package com.cmcabrera.cardcostapi.controller;

import com.cmcabrera.cardcostapi.dto.CardCostRequestDTO;
import com.cmcabrera.cardcostapi.dto.CardCostResponseDTO;
import com.cmcabrera.cardcostapi.service.CardCostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment-cards-cost")
@AllArgsConstructor
public class CardCostController {

    private final CardCostService cardCostService;

    @PostMapping
    public ResponseEntity<CardCostResponseDTO> calculateCardCost(@Valid @RequestBody CardCostRequestDTO requestDTO) {
        CardCostResponseDTO response = cardCostService.calculateCardCost(requestDTO.getCardNumber());
        return ResponseEntity.ok(response);
    }
}
