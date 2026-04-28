package com.luiz.medication_system.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.mongodb.core.index.Indexed;

public record EmployeeRequestDTO(
        String name,
        @NotBlank(message = "O campo CPF não pode estar vazio.")
        @CPF(message = "Erro de validação: O CPF informado não é válido.")
        @Indexed(unique = true)
        String cpf,
        @Indexed(unique = true)
        String registration,
        String password,
        String position,
        Boolean status
) {
}
