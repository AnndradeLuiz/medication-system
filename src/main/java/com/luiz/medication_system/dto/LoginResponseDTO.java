package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Employee;

public record LoginResponseDTO(
        String id, // ADICIONE ESTA LINHA
        String name
) {
    public LoginResponseDTO(Employee employee) {
        this(
                employee.getId(), // ADICIONE ESTA LINHA
                employee.getName()
        );
    }
}
