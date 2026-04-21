package com.luiz.medication_system.dto;

public record EmployeeRequestDTO(
        String name,
        String registration,
        String password,
        String position,
        Boolean status
) {
}
