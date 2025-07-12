package com.cmcabrera.cardcostapi.service;

import com.cmcabrera.cardcostapi.dto.CardCostResponseDTO;
import com.cmcabrera.cardcostapi.dto.binlookup.BinLookupResponseDTO;
import com.cmcabrera.cardcostapi.dto.binlookup.CountryInfoDTO;
import com.cmcabrera.cardcostapi.entity.ClearingCost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CardCostServiceTest {

    @Mock
    private BinLookupService binLookupService;

    @Mock
    private ClearingCostService clearingCostService;

    @InjectMocks
    private CardCostService cardCostService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup default clearing costs
        ClearingCost usCost = new ClearingCost();
        usCost.setCountryCode("US");
        usCost.setCost(new BigDecimal("5.00"));

        ClearingCost grCost = new ClearingCost();
        grCost.setCountryCode("GR");
        grCost.setCost(new BigDecimal("15.00"));

        ClearingCost otCost = new ClearingCost();
        otCost.setCountryCode("OT");
        otCost.setCost(new BigDecimal("10.00"));

        List<ClearingCost> allCosts = Arrays.asList(usCost, grCost, otCost);
        when(clearingCostService.findAll()).thenReturn(allCosts);
    }

    @Test
    void givenUsCardNumber_whenCalculateCardCost_thenReturnsUsCost() {
        // Given
        String cardNumber = "4571731234567890"; // US BIN
        String bin = cardNumber.substring(0, 6);

        BinLookupResponseDTO binLookupResponseDTO = new BinLookupResponseDTO();
        CountryInfoDTO countryInfoDTO = new CountryInfoDTO();
        countryInfoDTO.setAlpha2("US");
        binLookupResponseDTO.setCountry(countryInfoDTO);

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.of(binLookupResponseDTO));

        // When
        CardCostResponseDTO response = cardCostService.calculateCardCost(cardNumber);

        // Then
        assertEquals("US", response.getCountry());
        assertEquals(new BigDecimal("5.00"), response.getCost());
    }

    @Test
    void givenGrCardNumber_whenCalculateCardCost_thenReturnsGrCost() {
        // Given
        String cardNumber = "4571731234567890"; // GR BIN
        String bin = cardNumber.substring(0, 6);

        BinLookupResponseDTO binLookupResponseDTO = new BinLookupResponseDTO();
        CountryInfoDTO countryInfoDTO = new CountryInfoDTO();
        countryInfoDTO.setAlpha2("GR");
        binLookupResponseDTO.setCountry(countryInfoDTO);

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.of(binLookupResponseDTO));

        // When
        CardCostResponseDTO response = cardCostService.calculateCardCost(cardNumber);

        // Then
        assertEquals("GR", response.getCountry());
        assertEquals(new BigDecimal("15.00"), response.getCost());
    }

    @Test
    void givenOtherCardNumber_whenCalculateCardCost_thenReturnsOtCost() {
        // Given
        String cardNumber = "4571731234567890"; // Other BIN (e.g., DE)
        String bin = cardNumber.substring(0, 6);

        BinLookupResponseDTO binLookupResponseDTO = new BinLookupResponseDTO();
        CountryInfoDTO countryInfoDTO = new CountryInfoDTO();
        countryInfoDTO.setAlpha2("DE"); // Not US or GR
        binLookupResponseDTO.setCountry(countryInfoDTO);

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.of(binLookupResponseDTO));

        // When
        CardCostResponseDTO response = cardCostService.calculateCardCost(cardNumber);

        // Then
        assertEquals("DE", response.getCountry());
        assertEquals(new BigDecimal("10.00"), response.getCost());
    }

    @Test
    void givenBinLookupFails_whenCalculateCardCost_thenReturnsOtCost() {
        // Given
        String cardNumber = "1234567890123456";
        String bin = cardNumber.substring(0, 6);

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.empty());

        // When
        CardCostResponseDTO response = cardCostService.calculateCardCost(cardNumber);

        // Then
        assertEquals("OT", response.getCountry());
        assertEquals(new BigDecimal("10.00"), response.getCost());
    }

    @Test
    void givenBinLookupReturnsNullCountry_whenCalculateCardCost_thenReturnsOtCost() {
        // Given
        String cardNumber = "1234567890123456";
        String bin = cardNumber.substring(0, 6);

        BinLookupResponseDTO binLookupResponseDTO = new BinLookupResponseDTO();
        binLookupResponseDTO.setCountry(null); // Country is null

        when(binLookupService.lookupBin(bin)).thenReturn(Optional.of(binLookupResponseDTO));

        // When
        CardCostResponseDTO response = cardCostService.calculateCardCost(cardNumber);

        // Then
        assertEquals("OT", response.getCountry());
        assertEquals(new BigDecimal("10.00"), response.getCost());
    }
}
