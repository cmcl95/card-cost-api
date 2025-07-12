package com.cmcabrera.cardcostapi.controller;

import com.cmcabrera.cardcostapi.dto.ClearingCostDTO;
import com.cmcabrera.cardcostapi.entity.ClearingCost;
import com.cmcabrera.cardcostapi.repository.ClearingCostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class ClearingCostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClearingCostRepository clearingCostRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("TRUNCATE TABLE clearing_costs RESTART IDENTITY").executeUpdate();
    }

    @Test
    void givenClearingCostsExist_whenGetAllClearingCosts_thenReturnsListOfCosts() throws Exception {
        ClearingCost cost1 = new ClearingCost();
        cost1.setCountryCode("US");
        cost1.setCost(new BigDecimal("5.00"));
        clearingCostRepository.save(cost1);

        ClearingCost cost2 = new ClearingCost();
        cost2.setCountryCode("GR");
        cost2.setCost(new BigDecimal("15.00"));
        clearingCostRepository.save(cost2);

        ClearingCost cost3 = new ClearingCost();
        cost3.setCountryCode("OT");
        cost3.setCost(new BigDecimal("10.00"));
        clearingCostRepository.save(cost3);

        mockMvc.perform(get("/api/v1/clearing-costs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].countryCode", is("US")))
                .andExpect(jsonPath("$[0].cost", is(5.00)))
                .andExpect(jsonPath("$[1].countryCode", is("GR")))
                .andExpect(jsonPath("$[1].cost", is(15.00)))
                .andExpect(jsonPath("$[2].countryCode", is("OT")))
                .andExpect(jsonPath("$[2].cost", is(10.00)));
    }

    @Test
    void givenClearingCostExists_whenGetClearingCostById_thenReturnsCost() throws Exception {
        ClearingCost cost = new ClearingCost();
        cost.setCountryCode("US");
        cost.setCost(new BigDecimal("5.00"));
        clearingCostRepository.save(cost);

        mockMvc.perform(get("/api/v1/clearing-costs/" + cost.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode", is("US")))
                .andExpect(jsonPath("$.cost", is(5.00)));
    }

    @Test
    void givenClearingCostDoesNotExist_whenGetClearingCostById_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/clearing-costs/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNewClearingCost_whenCreateClearingCost_thenReturnsCreatedCost() throws Exception {
        ClearingCostDTO newCost = new ClearingCostDTO();
        newCost.setCountryCode("DE");
        newCost.setCost(new BigDecimal("7.50"));

        mockMvc.perform(post("/api/v1/clearing-costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCost)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.countryCode", is("DE")))
                .andExpect(jsonPath("$.cost", is(7.50)));

        mockMvc.perform(get("/api/v1/clearing-costs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void givenExistingClearingCost_whenUpdateClearingCost_thenReturnsUpdatedCost() throws Exception {
        ClearingCost existingCost = new ClearingCost();
        existingCost.setCountryCode("US");
        existingCost.setCost(new BigDecimal("5.00"));
        clearingCostRepository.save(existingCost);

        ClearingCostDTO updatedCost = new ClearingCostDTO();
        updatedCost.setCountryCode("US");
        updatedCost.setCost(new BigDecimal("6.00"));

        mockMvc.perform(put("/api/v1/clearing-costs/" + existingCost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode", is("US")))
                .andExpect(jsonPath("$.cost", is(6.00)));

        mockMvc.perform(get("/api/v1/clearing-costs/" + existingCost.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cost", is(6.00)));
    }

    @Test
    void givenNonExistingClearingCost_whenUpdateClearingCost_thenReturnsNotFound() throws Exception {
        ClearingCostDTO updatedCost = new ClearingCostDTO();
        updatedCost.setCountryCode("XX");
        updatedCost.setCost(new BigDecimal("100.00"));

        mockMvc.perform(put("/api/v1/clearing-costs/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCost)))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenExistingClearingCost_whenDeleteClearingCost_thenReturnsNoContent() throws Exception {
        ClearingCost existingCost = new ClearingCost();
        existingCost.setCountryCode("US");
        existingCost.setCost(new BigDecimal("5.00"));
        clearingCostRepository.save(existingCost);

        mockMvc.perform(delete("/api/v1/clearing-costs/" + existingCost.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/clearing-costs/" + existingCost.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNonExistingClearingCost_whenDeleteClearingCost_thenReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/clearing-costs/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenInvalidCountryCode_whenCreateClearingCost_thenReturns400() throws Exception {
        ClearingCostDTO invalidDto = new ClearingCostDTO();
        invalidDto.setCountryCode("USA"); // Invalid: too long
        invalidDto.setCost(new BigDecimal("10.00"));

        mockMvc.perform(post("/api/v1/clearing-costs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.details[0]").value("countryCode: size must be between 2 and 2"));
    }
}