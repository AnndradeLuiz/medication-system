package com.luiz.medication_system.dto;


import com.luiz.medication_system.dominio.ThirdParties;

public record ThirdPartiesResponseDTO(
        String name,
        String document,
        String observation
) {
    public ThirdPartiesResponseDTO(ThirdParties parties) {
        this(
                parties.getName(), parties.getDocument(), parties.getObservation()
        );
    }
}
