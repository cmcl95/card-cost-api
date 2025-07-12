package com.cmcabrera.cardcostapi.dto.binlookup;

import lombok.Data;

@Data
public class BinLookupResponseDTO {
    private NumberInfoDTO number;
    private String scheme;
    private String type;
    private String brand;
    private Boolean prepaid;
    private CountryInfoDTO country;
    private BankInfoDTO bank;
}
