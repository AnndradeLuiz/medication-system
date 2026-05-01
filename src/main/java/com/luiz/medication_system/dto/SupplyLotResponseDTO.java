package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.SupplyLot;

import java.time.Instant;

public record SupplyLotResponseDTO(
        String lotCode,
        Instant expirationDate,
        Integer receivedQuantity
) {
    public SupplyLotResponseDTO(SupplyLot lot) {
        this(
                lot.getLotCode(),
                lot.getExpirationDate(),
                lot.getReceivedQuantity()
        );
    }
}