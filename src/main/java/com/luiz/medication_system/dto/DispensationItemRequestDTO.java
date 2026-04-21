package com.luiz.medication_system.dto;


public record DispensationItemRequestDTO(
        String medicationId,
        Integer quantity
) {

}
