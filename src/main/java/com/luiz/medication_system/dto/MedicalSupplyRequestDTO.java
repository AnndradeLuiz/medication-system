package com.luiz.medication_system.dto;

import java.util.List;

public record MedicalSupplyRequestDTO(
        String name,
        String observation,
        List<LotRequestDTO> lots
) {

}
