package com.luiz.medication_system.dto;

import java.util.Date;
import java.util.List;

public record PatientRequestDTO(
        String name, String cpf, String cns,
        Date birthDate, List<String> phones,
        List<InclusionProgramRequestDTO> programs
) {

}
