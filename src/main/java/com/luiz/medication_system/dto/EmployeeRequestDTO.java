package com.luiz.medication_system.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record EmployeeRequestDTO(
        String name,
        @NotBlank(message = "O campo CPF não pode estar vazio.")
        @CPF(message = "Erro de validação: O CPF informado não é válido.")
        String cpf,
        String registration,
        String password,
        String position,
        Boolean status
) {
}
