package com.cmcabrera.cardcostapi.controller;

import com.cmcabrera.cardcostapi.dto.CardCostRequestDTO;
import com.cmcabrera.cardcostapi.dto.binlookup.BinLookupResponseDTO;
import com.cmcabrera.cardcostapi.dto.binlookup.CountryInfoDTO;
import com.cmcabrera.cardcostapi.entity.ClearingCost;
import com.cmcabrera.cardcostapi.repository.ClearingCostRepository;
import com.cmcabrera.cardcostapi.service.BinLookupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
public class CardCostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClearingCostRepository clearingCostRepository;

    @MockitoBean
    private BinLookupService binLookupService;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        clearingCostRepository.deleteAll();
        entityManager.createNativeQuery("TRUNCATE TABLE clearing_costs RESTART IDENTITY").executeUpdate();
    }

    @Test
    void givenUsCardNumber_whenCalculateCardCost_thenReturnsUsCost() throws Exception {
        // Given
        ClearingCost usCost = new ClearingCost();
        usCost.setCountryCode("US");
        usCost.setCost(new BigDecimal("5.00"));
        clearingCostRepository.save(usCost);

        ClearingCost grCost = new ClearingCost();
        grCost.setCountryCode("GR");
        grCost.setCost(new BigDecimal("15.00"));
        clearingCostRepository.save(grCost);

        ClearingCost otCost = new ClearingCost();
        otCost.setCountryCode("OT");
        otCost.setCost(new BigDecimal("10.00"));
        clearingCostRepository.save(otCost);

        String cardNumber = "4571731234567890";
        String bin = cardNumber.substring(0, 6);

        BinLookupResponseDTO binLookupResponseDTO = new BinLookupResponseDTO();
        CountryInfoDTO countryInfoDTO = new CountryInfoDTO();
        countryInfoDTO.setAlpha2("US");
        binLookupResponseDTO.setCountry(countryInfoDTO);

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.of(binLookupResponseDTO));

        CardCostRequestDTO requestDTO = new CardCostRequestDTO();
        requestDTO.setCardNumber(cardNumber);

        // When & Then
        mockMvc.perform(post("/api/v1/payment-cards-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country", is("US")))
                .andExpect(jsonPath("$.cost", is(5.00)));
    }

    @Test
    void givenGrCardNumber_whenCalculateCardCost_thenReturnsGrCost() throws Exception {
        // Given
        ClearingCost usCost = new ClearingCost();
        usCost.setCountryCode("US");
        usCost.setCost(new BigDecimal("5.00"));
        clearingCostRepository.save(usCost);

        ClearingCost grCost = new ClearingCost();
        grCost.setCountryCode("GR");
        grCost.setCost(new BigDecimal("15.00"));
        clearingCostRepository.save(grCost);

        ClearingCost otCost = new ClearingCost();
        otCost.setCountryCode("OT");
        otCost.setCost(new BigDecimal("10.00"));
        clearingCostRepository.save(otCost);

        String cardNumber = "4571731234567890";
        String bin = cardNumber.substring(0, 6);

        BinLookupResponseDTO binLookupResponseDTO = new BinLookupResponseDTO();
        CountryInfoDTO countryInfoDTO = new CountryInfoDTO();
        countryInfoDTO.setAlpha2("GR");
        binLookupResponseDTO.setCountry(countryInfoDTO);

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.of(binLookupResponseDTO));

        CardCostRequestDTO requestDTO = new CardCostRequestDTO();
        requestDTO.setCardNumber(cardNumber);

        // When & Then
        mockMvc.perform(post("/api/v1/payment-cards-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country", is("GR")))
                .andExpect(jsonPath("$.cost", is(15.00)));
    }

    @Test
    void givenOtherCardNumber_whenCalculateCardCost_thenReturnsOtCost() throws Exception {
        // Given
        ClearingCost usCost = new ClearingCost();
        usCost.setCountryCode("US");
        usCost.setCost(new BigDecimal("5.00"));
        clearingCostRepository.save(usCost);

        ClearingCost grCost = new ClearingCost();
        grCost.setCountryCode("GR");
        grCost.setCost(new BigDecimal("15.00"));
        clearingCostRepository.save(grCost);

        ClearingCost otCost = new ClearingCost();
        otCost.setCountryCode("OT");
        otCost.setCost(new BigDecimal("10.00"));
        clearingCostRepository.save(otCost);

        String cardNumber = "4571731234567890";
        String bin = cardNumber.substring(0, 6);

        BinLookupResponseDTO binLookupResponseDTO = new BinLookupResponseDTO();
        CountryInfoDTO countryInfoDTO = new CountryInfoDTO();
        countryInfoDTO.setAlpha2("DE");
        binLookupResponseDTO.setCountry(countryInfoDTO);

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.of(binLookupResponseDTO));

        CardCostRequestDTO requestDTO = new CardCostRequestDTO();
        requestDTO.setCardNumber(cardNumber);

        // When & Then
        mockMvc.perform(post("/api/v1/payment-cards-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country", is("DE")))
                .andExpect(jsonPath("$.cost", is(10.00)));
    }

    @Test
    void givenBinLookupFails_whenCalculateCardCost_thenReturnsOtCost() throws Exception {
        // Given
        ClearingCost usCost = new ClearingCost();
        usCost.setCountryCode("US");
        usCost.setCost(new BigDecimal("5.00"));
        clearingCostRepository.save(usCost);

        ClearingCost grCost = new ClearingCost();
        grCost.setCountryCode("GR");
        grCost.setCost(new BigDecimal("15.00"));
        clearingCostRepository.save(grCost);

        ClearingCost otCost = new ClearingCost();
        otCost.setCountryCode("OT");
        otCost.setCost(new BigDecimal("10.00"));
        clearingCostRepository.save(otCost);

        String cardNumber = "1234567890123456";
        String bin = cardNumber.substring(0, 6);

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.empty());

        CardCostRequestDTO requestDTO = new CardCostRequestDTO();
        requestDTO.setCardNumber(cardNumber);

        // When & Then
        mockMvc.perform(post("/api/v1/payment-cards-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country", is("OT")))
                .andExpect(jsonPath("$.cost", is(10.00)));
    }

    @Test
    void givenInvalidCardNumber_whenCalculateCardCost_thenReturnsBadRequest() throws Exception {
        // Given
        CardCostRequestDTO requestDTO = new CardCostRequestDTO();
        requestDTO.setCardNumber("123"); // Too short

        // When & Then
        mockMvc.perform(post("/api/v1/payment-cards-cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Validation Failed")))
                .andExpect(jsonPath("$.details[0]", is("cardNumber: size must be between 8 and 19")));
    }
}

