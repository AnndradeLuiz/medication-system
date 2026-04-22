package com.luiz.medication_system.dto;

public record MedicalSupplyItemRequestDTO(
        String medicalSupplyId,
        Integer quantity
) {
}
