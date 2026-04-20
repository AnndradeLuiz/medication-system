package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Patient;

import java.time.Instant;
import java.util.List;

public record PatientInsertDTO(
        String id, String name,
        String cpf, String cns,
        Instant birthDate, List<String> phones,
        List<InclusionProgramDTO> programs
) {
    public PatientInsertDTO(Patient patient) {
        this(
                patient.getId(), patient.getName(),
                patient.getCpf(), patient.getCns(),
                patient.getBirthDate(), patient.getPhones(),
                patient.getPrograms().stream().map(InclusionProgramDTO::new).toList()
        );
    }
}
