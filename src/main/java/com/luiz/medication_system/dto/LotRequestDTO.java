package com.luiz.medication_system.dto;

import java.time.Instant;

public record LotRequestDTO(
        String lotCode, Integer quantity, Instant expirationDate
) {

}
