package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.DispensationItem;
import com.luiz.medication_system.dominio.enums.PharmaceuticalFormEnum;
import com.luiz.medication_system.dominio.enums.ProgramCategory;

public record DispensationItemResponseDTO(
        String medicationId,
        String medicationName,
        String concentration,
        PharmaceuticalFormEnum PharmaceuticalForm,
        ProgramCategory programCategory,
        String lotCode,
        Integer quantity
) {
    public DispensationItemResponseDTO(DispensationItem entity) {
        this(
                entity.getMedicationId(),
                entity.getMedicationName(),
                entity.getConcentration(),
                entity.getPharmaceuticalForm(),
                entity.getProgramCategory(),
                entity.getLotCode(),
                entity.getQuantity()
        );
    }
}
