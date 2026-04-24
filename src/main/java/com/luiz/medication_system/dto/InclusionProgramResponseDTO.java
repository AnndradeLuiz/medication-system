package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.InclusionProgram;
import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;

import java.util.Date;


public record InclusionProgramResponseDTO(
        ProgramCategoryEnum name, Date inclusionDate, Boolean status
) {
    public InclusionProgramResponseDTO(InclusionProgram program) {
        this(
                program.getName(), program.getInclusionDate(), program.getStatus()
        );
    }
}
