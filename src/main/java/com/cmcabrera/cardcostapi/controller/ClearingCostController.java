package com.cmcabrera.cardcostapi.controller;

import com.cmcabrera.cardcostapi.dto.ApiErrorDTO;
import com.cmcabrera.cardcostapi.dto.ClearingCostDTO;
import com.cmcabrera.cardcostapi.entity.ClearingCost;
import com.cmcabrera.cardcostapi.mapper.ClearingCostMapper;
import com.cmcabrera.cardcostapi.service.ClearingCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clearing-costs")
@AllArgsConstructor
@Tag(name = "Clearing Costs", description = "CRUD operations for managing clearing costs")
public class ClearingCostController {

    private final ClearingCostService clearingCostService;
    private final ClearingCostMapper clearingCostMapper;

    @Operation(
            summary = "Get all clearing costs",
            description = "Retrieves a list of all configured clearing costs.",
            operationId = "getAllClearingCosts",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list of clearing costs",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ClearingCostDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Missing or invalid JWT token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    )
            }
    )
    @GetMapping
    public List<ClearingCostDTO> getAllClearingCosts() {
        return clearingCostService.findAll().stream()
                .map(clearingCostMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Get clearing cost by ID",
            description = "Retrieves a single clearing cost by its unique identifier.",
            operationId = "getClearingCostById",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved clearing cost",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClearingCostDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Missing or invalid JWT token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Clearing cost not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClearingCostDTO> getClearingCostById(@PathVariable Long id) {
        return clearingCostService.findById(id)
                .map(clearingCostMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Create a new clearing cost",
            description = "Adds a new clearing cost entry to the system.",
            operationId = "createClearingCost",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Clearing cost object to be created",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClearingCostDTO.class),
                            examples = @ExampleObject(value = "{\"countryCode\": \"ES\", \"cost\": 7.50}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Clearing cost created successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClearingCostDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Missing or invalid JWT token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClearingCostDTO createClearingCost(@Valid @org.springframework.web.bind.annotation.RequestBody ClearingCostDTO clearingCostDTO) {
        ClearingCost clearingCost = clearingCostMapper.toEntity(clearingCostDTO);
        return clearingCostMapper.toDto(clearingCostService.save(clearingCost));
    }

    @Operation(
            summary = "Update an existing clearing cost",
            description = "Updates an existing clearing cost identified by its ID.",
            operationId = "updateClearingCost",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated clearing cost object",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClearingCostDTO.class),
                            examples = @ExampleObject(value = "{\"countryCode\": \"ES\", \"cost\": 8.00}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Clearing cost updated successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ClearingCostDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Missing or invalid JWT token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Clearing cost not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ClearingCostDTO> updateClearingCost(@PathVariable Long id, @Valid @org.springframework.web.bind.annotation.RequestBody ClearingCostDTO clearingCostDTO) {
        return clearingCostService.findById(id)
                .map(existingCost -> {
                    existingCost.setCountryCode(clearingCostDTO.getCountryCode());
                    existingCost.setCost(clearingCostDTO.getCost());
                    ClearingCost updatedCost = clearingCostService.save(existingCost);
                    return ResponseEntity.ok(clearingCostMapper.toDto(updatedCost));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Delete a clearing cost",
            description = "Deletes a clearing cost entry by its ID.",
            operationId = "deleteClearingCost",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Clearing cost deleted successfully (No Content)"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Missing or invalid JWT token",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Clearing cost not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClearingCost(@PathVariable Long id) {
        return clearingCostService.findById(id)
                .map(clearingCost -> {
                    clearingCostService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
