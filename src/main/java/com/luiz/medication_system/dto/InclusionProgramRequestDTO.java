package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.InclusionProgram;

import java.time.Instant;

public record InclusionProgramRequestDTO(
        String name, Instant inclusionDate, Boolean status
) {
}
