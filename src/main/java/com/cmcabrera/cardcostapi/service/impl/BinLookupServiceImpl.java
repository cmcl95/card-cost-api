package com.cmcabrera.cardcostapi.service.impl;

import com.cmcabrera.cardcostapi.dto.binlookup.BinLookupResponseDTO;
import com.cmcabrera.cardcostapi.entity.BinCountryCache;
import com.cmcabrera.cardcostapi.repository.BinCountryCacheRepository;
import com.cmcabrera.cardcostapi.service.BinLookupService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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
    private final BinCountryCacheRepository binCountryCacheRepository;

    public BinLookupServiceImpl(RestTemplate restTemplate, BinCountryCacheRepository binCountryCacheRepository) {
        this.restTemplate = restTemplate;
        this.binCountryCacheRepository = binCountryCacheRepository;
    }

    // Setter for testing purposes
    public void setBinlistApiUrl(String binlistApiUrl) {
        this.binlistApiUrl = binlistApiUrl;
    }

    @Override
    @Cacheable(value = "binCache", key = "#bin")
    @CircuitBreaker(name = "binLookup", fallbackMethod = "lookupBinFallback")
    @Retry(name = "binLookup")
    public Optional<BinLookupResponseDTO> lookupBin(String bin) {
        return binCountryCacheRepository.findById(bin)
                .map(cached -> {
                    BinLookupResponseDTO response = new BinLookupResponseDTO();
                    response.getCountry().setAlpha2(cached.getCountry());
                    return Optional.of(response);
                })
                .orElseGet(() -> {
                    try {
                        String url = binlistApiUrl + bin;
                        BinLookupResponseDTO response = restTemplate.getForObject(url, BinLookupResponseDTO.class);
                        if (response != null && response.getCountry() != null) {
                            binCountryCacheRepository.save(new BinCountryCache(bin, response.getCountry().getAlpha2()));
                        }
                        return Optional.ofNullable(response);
                    } catch (Exception e) {
                        log.error("Error looking up BIN {}: {}", bin, e.getMessage());
                        return Optional.empty();
                    }
                });
    }

    public Optional<BinLookupResponseDTO> lookupBinFallback(String bin, Throwable t) {
        log.warn("Circuit breaker fallback for BIN {}: {}", bin, t.getMessage());
        return Optional.empty();
    }
}
