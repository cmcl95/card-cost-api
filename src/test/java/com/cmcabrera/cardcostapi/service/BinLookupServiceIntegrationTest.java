package com.cmcabrera.cardcostapi.service;

import com.cmcabrera.cardcostapi.dto.binlookup.BinLookupResponseDTO;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class BinLookupServiceIntegrationTest {

    @Autowired
    private BinLookupService binLookupService;

    private WireMockServer wireMockServer;

    @BeforeEach
    void setup() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void givenExternalServiceFails_whenLookupBinMultipleTimes_thenCircuitBreakerOpensAndReturnsEmpty() {
        String bin = "45717360";
        wireMockServer.stubFor(get(urlEqualTo("/" + bin))
                .willReturn(aResponse().withStatus(500)));

        for (int i = 0; i < 15; i++) {
            binLookupService.lookupBin(bin);
        }

        Optional<BinLookupResponseDTO> response = binLookupService.lookupBin(bin);
        assertTrue(response.isEmpty());
    }
}
