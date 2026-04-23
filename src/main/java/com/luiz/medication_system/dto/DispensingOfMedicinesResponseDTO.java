package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.DispensingOfMedicines;

import java.time.Instant;
import java.util.List;

public record DispensingOfMedicinesResponseDTO(
        String id,
        Instant moment,
        ResponsibleEmployeeResponseDTO employee,
        TargetPatientResponseDTO targetPatient,
        ThirdPartiesResponseDTO thirdPerson,
        List<DispensationItemResponseDTO> items
) {
    public DispensingOfMedicinesResponseDTO(DispensingOfMedicines entity) {
        this(
                entity.getId(),
                entity.getMoment(),
                new ResponsibleEmployeeResponseDTO(entity.getEmployee()),
                new TargetPatientResponseDTO(entity.getTargetPatient()),
                entity.getThirdPerson() != null ?  new ThirdPartiesResponseDTO(
                        entity.getThirdPerson().getName(),
                        entity.getThirdPerson().getDocument(),
                        entity.getThirdPerson().getObservation()
        ) : null,
                entity.getItems().stream().map(DispensationItemResponseDTO::new).toList()
        );
    }
}
