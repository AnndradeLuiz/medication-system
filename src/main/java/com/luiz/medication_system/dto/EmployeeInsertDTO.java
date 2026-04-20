package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Employee;

public record EmployeeInsertDTO(
        String id, String name,
        String registration, String password,
        String position, Boolean status
) {
    public EmployeeInsertDTO(Employee employee) {
        this(
                employee.getId(), employee.getName(),
                employee.getPassword(), employee.getRegistration(),
                employee.getPosition(), employee.getStatus()
        );
    }
}
