package com.luiz.medication_system.dto;

import java.util.List;

public record DispensingOfMedicinesRequestDTO(
        String employeeId,
        String patientId,
        ThirdPartiesRequestDTO thirdPerson,
        List<DispensationItemRequestDTO> items
) {

}
