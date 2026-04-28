package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Employee;

public record LoginResponseDTO(
        String id,
        String name
) {
    public LoginResponseDTO(Employee employee) {
        this(
                employee.getId(),
                employee.getName()
        );
    }
}
