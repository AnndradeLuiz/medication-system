package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Lot;

import java.time.Instant;

public record LotResponseDTO(
        String lotCode, Integer quantity, Instant expirationDate
) {
    public LotResponseDTO(Lot lot) {
        this(
                lot.getLotCode(), lot.getQuantity(), lot.getExpirationDate()
        );
    }
}
