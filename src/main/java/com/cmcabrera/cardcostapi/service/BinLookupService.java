package com.cmcabrera.cardcostapi.service;

import com.cmcabrera.cardcostapi.dto.binlookup.BinLookupResponseDTO;

import java.util.Optional;

public interface BinLookupService {
    Optional<BinLookupResponseDTO> lookupBin(String bin);
}
