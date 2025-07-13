package com.cmcabrera.cardcostapi.controller;

import com.cmcabrera.cardcostapi.dto.AuthenticationRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USERNAME = "test-user";
    private static final String PASSWORD = "password";

    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnsJwtToken() throws Exception {
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(USERNAME, PASSWORD);

        mockMvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").isNotEmpty());
    }

    @Test
    void givenInvalidCredentials_whenAuthenticate_thenReturnsUnauthorized() throws Exception {
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(USERNAME, "wrong-password");

        mockMvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenTooManyAuthenticationAttempts_whenAuthenticate_thenReturnsTooManyRequests() throws Exception {
        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO(USERNAME, PASSWORD);

        // Exceed the rate limit (3 attempts per minute for authenticationBucket)
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/v1/authenticate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk());
        }

        // The 4th attempt should be rate limited
        mockMvc.perform(post("/api/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isTooManyRequests());
    }
}
