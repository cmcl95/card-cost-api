package com.cmcabrera.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Authentication request details")
public class AuthenticationRequestDTO {
    @Schema(description = "Username for authentication", example = "test-user")
    private String username;
    @Schema(description = "Password for authentication", example = "password")
    private String password;
}
