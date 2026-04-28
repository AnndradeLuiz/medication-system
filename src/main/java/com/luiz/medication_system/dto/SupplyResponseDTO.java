package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Supply;

import java.util.List;

public record SupplyResponseDTO(
        String id,
        String name,
        String observation,
        List<LotResponseDTO> lots
) {
    public SupplyResponseDTO(Supply supply) {
        this(
                supply.getId(),
                supply.getName(),
                supply.getObservation(),
                supply.getLots().stream().map(LotResponseDTO::new).toList()
        );
    }
}
