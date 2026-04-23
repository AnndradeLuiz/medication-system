package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Medication;
import com.luiz.medication_system.dominio.enums.AdministrationRouteEnum;
import com.luiz.medication_system.dominio.enums.PharmaceuticalFormEnum;
import com.luiz.medication_system.dominio.enums.ProgramCategory;

import java.util.List;

public record MedicationResponseDTO(
        String id,
        String name,
        String activeIngredient,
        String concentration,

        PharmaceuticalFormEnum pharmaceuticalForm,
        AdministrationRouteEnum administrationRoute,

        ProgramCategory programCategory,
        String sigtapCode,

        Integer totalStock,
        List<LotResponseDTO> lots
) {
    public MedicationResponseDTO(Medication medication) {
        this(
                medication.getId(), medication.getName(), medication.getActiveIngredient(),
                medication.getConcentration(), medication.getPharmaceuticalForm(),
                medication.getAdministrationRoute(), medication.getProgramCategory(),
                medication.getSigtapCode(), medication.getTotalStock(),
                medication.getLots().stream().map(LotResponseDTO::new).toList()
        );
    }
}
