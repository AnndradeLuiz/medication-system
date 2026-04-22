package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.ResponsibleEmployee;

public record ResponsibleEmployeeResponseDTO(
        String employee,
        String name,
        String registration
) {
    public ResponsibleEmployeeResponseDTO(ResponsibleEmployee employee) {
        this(
                employee.getEmployeeId(),
                employee.getName(),
                employee.getRegistration()
        );
    }
}

