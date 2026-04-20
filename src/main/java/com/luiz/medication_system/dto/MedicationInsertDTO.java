package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.Medication;

import java.util.List;

public record MedicationInsertDTO(
        String id, String name,
        String category ,String pharmaceuticalForm,
        String unitOfMeasurement, String activeIngredient,
        String concentration, String sigtapCode, List<LotDTO> lots
) {
    public MedicationInsertDTO(Medication medication) {
        this(
                medication.getId(), medication.getName(),
                medication.getCategory(), medication.getPharmaceuticalForm(),
                medication.getUnitOfMeasurement(), medication.getActiveIngredient(),
                medication.getConcentration(), medication.getSigtapCode(),
                medication.getLots().stream().map(LotDTO::new).toList()
        );
    }
}
