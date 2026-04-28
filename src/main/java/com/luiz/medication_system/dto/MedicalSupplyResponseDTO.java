package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.MedicalSupply;

import java.util.List;

public record MedicalSupplyResponseDTO(
        String id,
        String name,
        String observation,
        List<LotResponseDTO> lots
) {
    public MedicalSupplyResponseDTO(MedicalSupply medicalSupply) {
        this(
                medicalSupply.getId(),
                medicalSupply.getName(),
                medicalSupply.getObservation(),
                medicalSupply.getLots().stream().map(LotResponseDTO::new).toList()
        );
    }
}
