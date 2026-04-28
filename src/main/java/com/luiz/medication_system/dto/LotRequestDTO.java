package com.luiz.medication_system.dto;

import java.time.Instant;

public record LotRequestDTO(
        String lotCode, Instant expirationDate, Integer quantity
        ) {

}
