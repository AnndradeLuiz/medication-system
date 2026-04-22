package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Medication;

public record MedicationResponseDTO(
        String id,
        String name,
        String category,
        String pharmaceuticalForm,
        String unitOfMeasurement,
        String activeIngredient,
        String concentration,
        String sigtapCode,
        Integer totalStock
) {
    public MedicationResponseDTO(Medication medication) {
        this(
                medication.getId(), medication.getName(),
                medication.getCategory(), medication.getPharmaceuticalForm(),
                medication.getUnitOfMeasurement(), medication.getActiveIngredient(),
                medication.getConcentration(), medication.getSigtapCode(), medication.getTotalStock()
        );
    }
}
