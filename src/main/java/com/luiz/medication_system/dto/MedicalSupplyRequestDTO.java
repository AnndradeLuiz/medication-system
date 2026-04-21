package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.Lot;

import java.util.List;

public record MedicalSupplyRequestDTO(
        String name,
        String observation,
        String sigtapCode,
        List<Lot> lots
) {

}
