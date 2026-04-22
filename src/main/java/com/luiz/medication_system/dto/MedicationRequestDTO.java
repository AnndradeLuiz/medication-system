package com.luiz.medication_system.dto;

import java.util.List;

public record MedicationRequestDTO(
        String name,
        String category,
        String pharmaceuticalForm,
        String unitOfMeasurement,
        String activeIngredient,
        String concentration,
        String sigtapCode,
        List<LotRequestDTO> lots
) {

}
