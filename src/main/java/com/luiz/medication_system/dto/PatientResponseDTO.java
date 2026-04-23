package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Patient;

import java.util.Date;
import java.util.List;

public record PatientResponseDTO(
        String id, String name,
        String cpf, String cns,
        Date birthDate, List<String> phones,
        List<InclusionProgramResponseDTO> programs
) {
    public PatientResponseDTO(Patient patient) {
        this(
                patient.getId(), patient.getName(),
                patient.getCpf(), patient.getCns(),
                patient.getBirthDate(), patient.getPhones(),
                patient.getPrograms() != null ?
                        patient.getPrograms().stream().map(InclusionProgramResponseDTO::new).toList() : List.of()
        );
    }
}
