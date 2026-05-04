package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Employee;
import com.luiz.medication_system.dominio.enums.Role;

public record EmployeeResponseDTO(
        String id, String name, String cpf,
        String registration, Role role, Boolean status
) {
    public EmployeeResponseDTO(Employee employee) {
        this(
                employee.getId(), employee.getName(), employee.getCpf(),
                employee.getRegistration(), employee.getRole(), employee.getStatus()
        );
    }
}
