package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Employee;
import com.luiz.medication_system.dominio.enums.Role;

public record LoginResponseDTO(
        String id,
        String name,
        String registration,
        Role role,
        Boolean status,
        String token
) {
    public LoginResponseDTO(Employee employee, String token) {
        this(
                employee.getId(),
                employee.getName(),
                employee.getRegistration(),
                employee.getRole(),
                employee.getStatus(),
                token
        );
    }
}