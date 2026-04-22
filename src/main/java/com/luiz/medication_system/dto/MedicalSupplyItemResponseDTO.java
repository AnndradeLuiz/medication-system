package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.MedicalSupplyItem;

public record MedicalSupplyItemResponseDTO(
        String medicalSupplyId,
        String name,
        String lotCode,
        Integer quantity
) {
    public MedicalSupplyItemResponseDTO(MedicalSupplyItem supply) {
        this(
                supply.getMedicalSupplyId(),
                supply.getName(),
                supply.getLotCode(),
                supply.getQuantity()
        );
    }
}
