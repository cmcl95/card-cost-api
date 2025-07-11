package com.cmcabrera.cardcostapi.service;

import com.cmcabrera.cardcostapi.entity.ClearingCost;
import com.cmcabrera.cardcostapi.repository.ClearingCostRepository;
import com.cmcabrera.cardcostapi.service.impl.ClearingCostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClearingCostServiceTest {

    @Mock
    private ClearingCostRepository clearingCostRepository;

    @InjectMocks
    private ClearingCostServiceImpl clearingCostService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenClearingCostsExist_whenFindAll_thenAllClearingCostsAreReturned() {
        ClearingCost cost1 = new ClearingCost();
        cost1.setId(1L);
        cost1.setCountryCode("US");
        cost1.setCost(new BigDecimal("5.00"));

        ClearingCost cost2 = new ClearingCost();
        cost2.setId(2L);
        cost2.setCountryCode("GR");
        cost2.setCost(new BigDecimal("15.00"));

        List<ClearingCost> expectedCosts = Arrays.asList(cost1, cost2);
        when(clearingCostRepository.findAll()).thenReturn(expectedCosts);

        List<ClearingCost> actualCosts = clearingCostService.findAll();

        assertNotNull(actualCosts);
        assertEquals(2, actualCosts.size());
        assertEquals(expectedCosts, actualCosts);
        verify(clearingCostRepository, times(1)).findAll();
    }

    @Test
    void givenClearingCostExists_whenFindById_thenClearingCostIsReturned() {
        ClearingCost cost = new ClearingCost();
        cost.setId(1L);
        cost.setCountryCode("US");
        cost.setCost(new BigDecimal("5.00"));

        when(clearingCostRepository.findById(1L)).thenReturn(Optional.of(cost));

        Optional<ClearingCost> actualCost = clearingCostService.findById(1L);

        assertTrue(actualCost.isPresent());
        assertEquals(cost, actualCost.get());
        verify(clearingCostRepository, times(1)).findById(1L);
    }

    @Test
    void givenClearingCostDoesNotExist_whenFindById_thenEmptyOptionalIsReturned() {
        when(clearingCostRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ClearingCost> actualCost = clearingCostService.findById(1L);

        assertFalse(actualCost.isPresent());
        verify(clearingCostRepository, times(1)).findById(1L);
    }

    @Test
    void givenNewClearingCost_whenSave_thenSavedClearingCostIsReturned() {
        ClearingCost costToSave = new ClearingCost();
        costToSave.setCountryCode("FR");
        costToSave.setCost(new BigDecimal("12.00"));

        ClearingCost savedCost = new ClearingCost();
        savedCost.setId(3L);
        savedCost.setCountryCode("FR");
        savedCost.setCost(new BigDecimal("12.00"));
        savedCost.setCreatedAt(LocalDateTime.now());
        savedCost.setUpdatedAt(LocalDateTime.now());

        when(clearingCostRepository.save(costToSave)).thenReturn(savedCost);

        ClearingCost actualSavedCost = clearingCostService.save(costToSave);

        assertNotNull(actualSavedCost.getId());
        assertEquals(savedCost, actualSavedCost);
        verify(clearingCostRepository, times(1)).save(costToSave);
    }

    @Test
    void givenClearingCostId_whenDeleteById_thenRepositoryDeleteIsCalled() {
        doNothing().when(clearingCostRepository).deleteById(1L);

        clearingCostService.deleteById(1L);

        verify(clearingCostRepository, times(1)).deleteById(1L);
    }
}
