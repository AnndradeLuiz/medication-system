package com.luiz.medication_system.dto;

public record SupplyItemRequestDTO(
        String medicalSupplyId,
        Integer quantity
) {
}
