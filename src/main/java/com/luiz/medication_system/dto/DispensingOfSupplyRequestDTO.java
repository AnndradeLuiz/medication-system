package com.luiz.medication_system.dto;

import java.util.List;

public record DispensingOfSupplyRequestDTO(
        String employeeId,
        String observation,
        List<MedicalSupplyItemRequestDTO> supplies
) {
}
