package com.luiz.medication_system.dto;

public record TargetPatientRequestDTO(
        String name,
        String cpf,
        String cns
) {
}
