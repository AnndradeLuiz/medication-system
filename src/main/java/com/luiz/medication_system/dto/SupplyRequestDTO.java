package com.luiz.medication_system.dto;

import java.util.List;

public record SupplyRequestDTO(
        String name,
        String observation,
        List<LotRequestDTO> lots
) {

}
