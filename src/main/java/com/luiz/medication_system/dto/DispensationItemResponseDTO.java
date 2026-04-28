package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.MedicationItem;
import com.luiz.medication_system.dominio.enums.PharmaceuticalFormEnum;
import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;

public record DispensationItemResponseDTO(
        String medicationId,
        String medicationName,
        String concentration,
        PharmaceuticalFormEnum PharmaceuticalForm,
        ProgramCategoryEnum programCategoryEnum,
        String lotCode,
        Integer quantity
) {
    public DispensationItemResponseDTO(MedicationItem entity) {
        this(
                entity.getMedicationId(),
                entity.getActiveIngredient(),
                entity.getConcentration(),
                entity.getPharmaceuticalForm(),
                entity.getProgramCategoryEnum(),
                entity.getLotCode(),
                entity.getQuantity()
        );
    }
}
