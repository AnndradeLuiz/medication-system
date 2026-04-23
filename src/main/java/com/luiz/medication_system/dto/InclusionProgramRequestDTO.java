package com.luiz.medication_system.dto;

import java.util.Date;

public record InclusionProgramRequestDTO(
        String name, Date inclusionDate, Boolean status
) {
}
