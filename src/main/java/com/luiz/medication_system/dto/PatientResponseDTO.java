package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Patient;

import java.time.LocalDate;
import java.util.List;

public record PatientResponseDTO(
        String id,
        String name,
        String cpf,
        String cns,
        LocalDate birthDate,
        Integer age,
        Boolean status,
        Boolean external,
        List<String> phones,
        List<InclusionProgramResponseDTO> programs
) {
    public PatientResponseDTO(Patient patient) {
        this(
                patient.getId(),
                patient.getName(),
                patient.getCpf(),
                patient.getCns(),
                patient.getBirthDate(),
                patient.getAge(),
                patient.getStatus(),
                patient.getExternal(),
                patient.getPhones(),
                patient.getPrograms() != null ?
                        patient.getPrograms().stream().map(InclusionProgramResponseDTO::new).toList() : List.of()
        );
    }
}
