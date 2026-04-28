package com.luiz.medication_system.dto;

import java.time.LocalDate;

public record LotRequestDTO(
        String lotCode, LocalDate expirationDate, Integer quantity
        ) {

}
