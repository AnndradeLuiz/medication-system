package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.List;

public record PatientRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        String name,
        @CPF(message = "Erro de validação: O CPF informado não é válido.")
        @Indexed(unique = true)
        String cpf,
        @Indexed(unique = true)
        String cns,
        @PastOrPresent(message = "A data de nascimento não pode estar no futuro")
        Instant birthDate,
        Boolean status,
        Boolean external,
        List<String> phones,
        List<ProgramCategoryEnum> programs
) {

}
