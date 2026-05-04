package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Employee;
import com.luiz.medication_system.dominio.enums.Role;

public record LoginResponseDTO(
        String id,
        String name,
        String registration,
        Role role
) {
    public LoginResponseDTO(Employee employee) {
        this(
                employee.getId(),
                employee.getName(),
                employee.getRegistration(),
                employee.getRole()
        );
    }
}
