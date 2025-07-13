package com.cmcabrera.cardcostapi.controller;

import com.cmcabrera.cardcostapi.dto.ApiErrorDTO;
import com.cmcabrera.cardcostapi.dto.AuthenticationRequestDTO;
import com.cmcabrera.cardcostapi.dto.AuthenticationResponseDTO;
import com.cmcabrera.cardcostapi.service.UserDetailsServiceImpl;
import com.cmcabrera.cardcostapi.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authenticate")
@Tag(name = "Authentication", description = "API for user authentication and JWT token generation")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "Authenticate user and return JWT token",
            description = "Authenticates a user with provided credentials and returns a JWT token for accessing protected resources.",
            operationId = "authenticateUser",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User authentication request",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AuthenticationRequestDTO.class),
                            examples = @ExampleObject(value = "{\"username\": \"test-user\", \"password\": \"password\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication successful, JWT token returned",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthenticationResponseDTO.class),
                                    examples = @ExampleObject(value = "{\"jwt\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0LXVzZXIiLCJpYXQiOjE2NzgyMzU2NzgsImV4cCI6MTY3ODIzOTI3OH0.signature\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid username or password",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"timestamp\": \"12-07-2025 10:30:00\", \"status\": 401, \"error\": \"Unauthorized\", \"details\": [\"Bad credentials\"]}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "429",
                            description = "Too many requests",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"timestamp\": \"12-07-2025 10:30:00\", \"status\": 429, \"error\": \"Too many requests\", \"details\": [\"Too many authentication attempts. Please try again later.\"]}")
                            )
                    ),
            }
    )
    @PostMapping
    public ResponseEntity<AuthenticationResponseDTO> createAuthenticationToken(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getUsername(), authenticationRequestDTO.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequestDTO.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponseDTO(jwt));
    }
}
