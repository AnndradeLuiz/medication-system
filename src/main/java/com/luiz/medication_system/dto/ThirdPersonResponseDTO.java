package com.luiz.medication_system.dto;


import com.luiz.medication_system.dominio.ThirdPerson;

public record ThirdPersonResponseDTO(
        String name,
        String document,
        String observation
) {
    public ThirdPersonResponseDTO(ThirdPerson parties) {
        this(
                parties.getName(), parties.getDocument(), parties.getObservation()
        );
    }
}
