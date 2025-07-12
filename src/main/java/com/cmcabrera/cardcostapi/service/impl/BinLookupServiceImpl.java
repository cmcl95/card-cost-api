package com.cmcabrera.cardcostapi.service.impl;

import com.cmcabrera.cardcostapi.dto.binlookup.BinLookupResponseDTO;
import com.cmcabrera.cardcostapi.service.BinLookupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class BinLookupServiceImpl implements BinLookupService {

    @Value("${binlist.api.url}")
    private String binlistApiUrl;

    private final RestTemplate restTemplate;

    public BinLookupServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Setter for testing purposes
    public void setBinlistApiUrl(String binlistApiUrl) {
        this.binlistApiUrl = binlistApiUrl;
    }

    @Override
    @Cacheable(value = "binCache", key = "#bin")
    public Optional<BinLookupResponseDTO> lookupBin(String bin) {
        try {
            String url = binlistApiUrl + bin;
            BinLookupResponseDTO response = restTemplate.getForObject(url, BinLookupResponseDTO.class);
            return Optional.ofNullable(response);
        } catch (Exception e) {
            log.error("Error looking up BIN {}: {}", bin, e.getMessage());
            return Optional.empty();
        }
    }
}
