package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.SupplyItem;

public record SupplyItemResponseDTO(
        String medicalSupplyId,
        String name,
        String lotCode,
        Integer quantity
) {
    public SupplyItemResponseDTO(SupplyItem supply) {
        this(
                supply.getMedicalSupplyId(),
                supply.getName(),
                supply.getLotCode(),
                supply.getQuantity()
        );
    }
}
