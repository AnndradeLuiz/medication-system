package com.luiz.medication_system.dto;

public record LowStockAlertDTO(
        String medicationName,
        Integer currentQuantity,
        String closestExpirationDate,
        String status
) {
}
