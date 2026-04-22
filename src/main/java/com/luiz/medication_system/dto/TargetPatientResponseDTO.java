package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.TargetPatient;

public record TargetPatientResponseDTO(
        String patientId,
        String name,
        String cpf,
        String cns
) {
    public TargetPatientResponseDTO(TargetPatient patient) {
        this(
                patient.getPatientId(),
                patient.getName(),
                patient.getCpf(),
                patient.getCns()
        );
    }
}
