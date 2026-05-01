package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Supply;
import com.luiz.medication_system.dominio.SupplyLot;

import java.util.List;

public record SupplyResponseDTO(
        String id,
        String name,
        String observation,
        List<SupplyLotResponseDTO> lots
) {
    public SupplyResponseDTO(Supply supply) {
        this(
                supply.getId(),
                supply.getName(),
                supply.getObservation(),
                supply.getLots().stream().map(SupplyLotResponseDTO::new).toList()
        );
    }
}
