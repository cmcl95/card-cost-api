package com.cmcabrera.cardcostapi.dto.binlookup;

import lombok.Data;

@Data
public class CountryInfoDTO {
    private String numeric;
    private String alpha2;
    private String name;
    private String emoji;
    private String currency;
    private Integer latitude;
    private Integer longitude;
}
