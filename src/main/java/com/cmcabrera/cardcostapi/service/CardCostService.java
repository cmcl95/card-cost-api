package com.cmcabrera.cardcostapi.service;

import com.cmcabrera.cardcostapi.dto.CardCostResponseDTO;
import com.cmcabrera.cardcostapi.dto.binlookup.BinLookupResponseDTO;
import com.cmcabrera.cardcostapi.entity.ClearingCost;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CardCostService {

    private final BinLookupService binLookupService;
    private final ClearingCostService clearingCostService;

    public CardCostResponseDTO calculateCardCost(String cardNumber) {
        String bin = cardNumber.substring(0, 6);
        Optional<BinLookupResponseDTO> binLookupResponse = binLookupService.lookupBin(bin);

        String countryCode = binLookupResponse
                .map(response -> response.getCountry() != null ? response.getCountry().getAlpha2() : "OT")
                .orElse("OT"); // Default to 'OT' if BIN lookup fails or country is null

        Optional<ClearingCost> clearingCost = clearingCostService.findAll().stream()
                .filter(cost -> cost.getCountryCode().equals(countryCode))
                .findFirst();

        BigDecimal cost = clearingCost
                .map(ClearingCost::getCost)
                .orElseGet(() -> {
                    // Fallback to 'OT' cost if specific country not found
                    return clearingCostService.findAll().stream()
                            .filter(costEntry -> costEntry.getCountryCode().equals("OT"))
                            .map(ClearingCost::getCost)
                            .findFirst()
                            .orElse(BigDecimal.ZERO); // Default to 0 if 'OT' not found (should not happen)
                });

        return new CardCostResponseDTO(countryCode, cost);
    }
}
