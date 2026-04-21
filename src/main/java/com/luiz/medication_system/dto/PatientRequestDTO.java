package com.luiz.medication_system.dto;

import java.time.Instant;
import java.util.List;

public record PatientRequestDTO(
        String name, String cpf, String cns,
        Instant birthDate, List<String> phones,
        List<InclusionProgramRequestDTO> programs
) {

}
