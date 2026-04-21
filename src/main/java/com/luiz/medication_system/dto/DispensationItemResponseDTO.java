package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.DispensationItem;

public record DispensationItemResponseDTO(
        String medicationId,
        String medicationName,
        String concentration,
        String lotCode,
        Integer quantity
) {
    public DispensationItemResponseDTO(DispensationItem entity) {
        this(
                entity.getMedicationId(),
                entity.getMedicationName(),
                entity.getConcentration(),
                entity.getLotCode(),
                entity.getQuantity()
        );
    }
}
