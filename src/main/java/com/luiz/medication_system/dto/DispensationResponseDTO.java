package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Dispensation;

import java.time.Instant;
import java.util.List;

public record DispensationResponseDTO(
        String id,
        Instant moment,
        ResponsibleEmployeeResponseDTO employee,
        TargetPatientResponseDTO targetPatient,
        ThirdPartiesResponseDTO parties,
        List<DispensationItemResponseDTO> items
) {
    public DispensationResponseDTO(Dispensation entity) {
        this(
                entity.getId(),
                entity.getMoment(),
                new ResponsibleEmployeeResponseDTO(entity.getEmployee()),
                new TargetPatientResponseDTO(entity.getTargetPatient()),
                entity.getParties() != null ?  new ThirdPartiesResponseDTO(
                        entity.getParties().getName(),
                        entity.getParties().getDocument(),
                        entity.getParties().getObservation()
        ) : null,
                entity.getItems().stream().map(DispensationItemResponseDTO::new).toList()
        );
    }
}
