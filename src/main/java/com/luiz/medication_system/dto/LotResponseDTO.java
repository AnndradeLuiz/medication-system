package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Lot;

import java.util.Date;

public record LotResponseDTO(
        String laboratory,
        String lotCode,
        Date expirationDate,
        Integer initialQuantity,
        Integer currentQuantity
) {
    public LotResponseDTO(Lot lot) {
        this(
                lot.getLaboratory(),
                lot.getLotCode(),
                lot.getExpirationDate(),
                lot.getInitialQuantity(),
                lot.getCurrentQuantity()
        );
    }
}
