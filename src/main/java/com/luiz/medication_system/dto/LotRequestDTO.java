package com.luiz.medication_system.dto;

import java.util.Date;

public record LotRequestDTO(
        String laboratory, String lotCode, Date expirationDate, Integer quantity
        ) {

}
