package com.cmcabrera.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Authentication response details with JWT token")
public class AuthenticationResponseDTO {
    @Schema(description = "Generated JWT token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJpYXQiOjE2NzgyMzU2NzgsImV4cCI6MTY3ODIzOTI3OH0.signature")
    private String jwt;
}
