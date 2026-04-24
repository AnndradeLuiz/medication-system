package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;

import java.util.Date;

public record InclusionProgramRequestDTO(
        ProgramCategoryEnum name, Date inclusionDate, Boolean status
) {
}
