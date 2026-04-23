package com.luiz.medication_system.dto;


import com.luiz.medication_system.dominio.ThirdPerson;

public record ThirdPartiesResponseDTO(
        String name,
        String document,
        String observation
) {
    public ThirdPartiesResponseDTO(ThirdPerson parties) {
        this(
                parties.getName(), parties.getDocument(), parties.getObservation()
        );
    }
}
