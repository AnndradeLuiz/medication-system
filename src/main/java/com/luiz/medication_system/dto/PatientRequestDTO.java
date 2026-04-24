package com.luiz.medication_system.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;
import java.util.List;

public record PatientRequestDTO(
        String name,
        @NotBlank(message = "O campo CPF não pode estar vazio.")
        @CPF(message = "Erro de validação: O CPF informado não é válido.")
        String cpf,
        String cns,
        Date birthDate,
        List<String> phones,
        List<InclusionProgramRequestDTO> programs
) {

}
