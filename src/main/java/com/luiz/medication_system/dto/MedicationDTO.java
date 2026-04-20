package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.Medication;

import java.util.List;

public record MedicationDTO(
        String id, String name,
        String category ,String pharmaceuticalForm,
        String unitOfMeasurement, String activeIngredient,
        String concentration, String sigtapCode, Integer totalStock
) {
    public MedicationDTO(Medication medication) {
        this(
                medication.getId(), medication.getName(),
                medication.getCategory(), medication.getPharmaceuticalForm(),
                medication.getUnitOfMeasurement(), medication.getActiveIngredient(),
                medication.getConcentration(), medication.getSigtapCode(), medication.getTotalStock()
        );
    }
}
