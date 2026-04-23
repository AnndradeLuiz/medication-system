package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.InclusionProgram;

import java.util.Date;


public record InclusionProgramResponseDTO(
        String name, Date inclusionDate, Boolean status
) {
    public InclusionProgramResponseDTO(InclusionProgram program) {
        this(
                program.getName(), program.getInclusionDate(), program.getStatus()
        );
    }
}
