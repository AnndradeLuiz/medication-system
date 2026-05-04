package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.SupplyLot;

import java.util.List;

public record FacilitySupplyRequestDTO(
        String name,
        String observation,
        List<LotRequestDTO> lots
) {
}
