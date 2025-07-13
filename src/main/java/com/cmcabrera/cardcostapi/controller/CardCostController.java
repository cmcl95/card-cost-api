package com.cmcabrera.cardcostapi.controller;

import com.cmcabrera.cardcostapi.dto.ApiErrorDTO;
import com.cmcabrera.cardcostapi.dto.CardCostRequestDTO;
import com.cmcabrera.cardcostapi.dto.CardCostResponseDTO;
import com.cmcabrera.cardcostapi.service.CardCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment-cards-cost")
@AllArgsConstructor
@Tag(name = "Card Cost", description = "API for calculating payment card costs")
public class CardCostController {

    private final CardCostService cardCostService;

    @Operation(
            summary = "Calculate payment card cost",
            description = "Calculates the clearing cost of a given payment card number based on its issuing country.",
            operationId = "calculateCardCost",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Card number to calculate cost for",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CardCostRequestDTO.class),
                            examples = @ExampleObject(value = "{\"card_number\": \"4571736000000000\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully calculated card cost",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CardCostResponseDTO.class),
                                    examples = @ExampleObject(value = "{\"country\": \"US\", \"cost\": 5.00}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid card number provided",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"timestamp\": \"12-07-2025 10:30:00\", \"status\": 400, \"error\": \"Validation Failed\", \"details\": [\"cardNumber: size must be between 8 and 19\"]}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Missing or invalid JWT token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"timestamp\": \"12-07-2025 10:30:00\", \"status\": 401, \"error\": \"Unauthorized\", \"details\": [\"Full authentication is required to access this resource\"]}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "429",
                            description = "Too many requests",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"timestamp\": \"12-07-2025 10:30:00\", \"status\": 429, \"error\": \"Too many requests\", \"details\": [\"Too many requests for payment cards cost. Please try again later.\"]}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"timestamp\": \"12-07-2025 10:30:00\", \"status\": 500, \"error\": \"Internal Server Error\", \"details\": [\"An unexpected error occurred\"]}")
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CardCostResponseDTO> calculateCardCost(@Valid @RequestBody CardCostRequestDTO requestDTO) {
        CardCostResponseDTO response = cardCostService.calculateCardCost(requestDTO.getCardNumber());
        return ResponseEntity.ok(response);
    }
}
