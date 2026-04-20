package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.InclusionProgram;

import java.time.Instant;

public record InclusionProgramDTO(
        String name, Instant inclusionDate, Boolean status
) {
    public InclusionProgramDTO(InclusionProgram program) {
        this(
                program.getName(), program.getInclusionDate(), program.getStatus()
        );
    }
}
