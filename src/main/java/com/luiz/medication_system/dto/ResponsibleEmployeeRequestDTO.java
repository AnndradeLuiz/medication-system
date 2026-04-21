package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.ResponsibleEmployee;

public record ResponsibleEmployeeRequestDTO(
        String employeeId,
        String name,
        String registration
) {

}

