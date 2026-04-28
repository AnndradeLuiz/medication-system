package com.luiz.medication_system.dto;

public record LowStockAlertDTO(
        String medicationActiveIngredient,
        String medicationConcentration,
        Integer currentQuantity,
        String closestExpirationDate,
        String status
) {
}
