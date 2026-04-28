package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Lot;

import java.time.LocalDate;
import java.util.Date;

public record LotResponseDTO(
        String lotCode,
        LocalDate expirationDate,
        Integer initialQuantity,
        Integer currentQuantity
) {
    public LotResponseDTO(Lot lot) {
        this(
                lot.getLotCode(),
                lot.getExpirationDate(),
                lot.getInitialQuantity(),
                lot.getCurrentQuantity()
        );
    }
}
