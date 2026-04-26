package com.luiz.medication_system.dto;

import java.time.LocalDate;

public record LotRequestDTO(
        String laboratory, String lotCode, LocalDate expirationDate, Integer quantity
        ) {

}
