package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Employee;

public record EmployeeResponseDTO(
        String id, String name, String cpf,String registration,
        String position, Boolean status
) {
    public EmployeeResponseDTO(Employee employee) {
        this(
                employee.getId(), employee.getName(), employee.getCpf(), employee.getRegistration(),
                employee.getPosition(), employee.getStatus()
        );
    }
}
