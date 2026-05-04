package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.FacilitySupply;

import java.util.List;

public record FacilitySupplyResponseDTO(
        String id,
        String name,
        String observation,
        List<SupplyLotResponseDTO> lots
) {
    public FacilitySupplyResponseDTO(FacilitySupply supply) {
        this(
                supply.getId(),
                supply.getName(),
                supply.getObservation(),
                supply.getLots().stream().map(SupplyLotResponseDTO::new).toList()
        );
    }
}
