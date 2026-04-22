package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.ResponsibleEmployee;

public record ResponsibleEmployeeRequestDTO(
        String employee,
        String name,
        String registration
) {

}

