package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.DispensingOfSupply;

import java.time.Instant;
import java.util.List;

public record DispensingOfSupplyResponseDTO(
        String id,
        Instant moment,
        ResponsibleEmployeeResponseDTO employeeId,
        String observation,
        List<MedicalSupplyItemResponseDTO> supplies
) {
    public DispensingOfSupplyResponseDTO(DispensingOfSupply supply) {
        this(
                supply.getId(),
                supply.getMoment(),
                new ResponsibleEmployeeResponseDTO(supply.getEmployee()),
                supply.getObservation(),
                supply.getSupplies().stream().map(MedicalSupplyItemResponseDTO::new).toList()
        );
    }
}
