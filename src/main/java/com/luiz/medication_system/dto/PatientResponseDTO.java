package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Patient;
import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;

import java.time.Instant;
import java.util.List;

public record PatientResponseDTO(
        String id,
        String name,
        String cpf,
        String cns,
        Instant birthDate,
        Integer age,
        Boolean status,
        Boolean external,
        List<String> phones,
        List<ProgramCategoryEnum> programs
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
                        patient.getPrograms() : List.of()
        );
    }
}
