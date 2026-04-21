package com.luiz.medication_system.dto;

import java.util.List;

public record DispensationRequestDTO(
        String employeeId,
        String patientId,
        ThirdPartiesRequestDTO parties,
        List<DispensationItemRequestDTO> items
) {

}
