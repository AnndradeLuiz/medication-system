package com.luiz.medication_system.dto;

import com.luiz.medication_system.dominio.enums.AdministrationRouteEnum;
import com.luiz.medication_system.dominio.enums.PharmaceuticalFormEnum;
import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.util.List;

public record MedicationRequestDTO(
        String activeIngredient,
        String concentration,

        PharmaceuticalFormEnum pharmaceuticalForm,
        AdministrationRouteEnum administrationRoute,

        ProgramCategoryEnum programCategoryEnum,

        List<LotRequestDTO> lots
) {

}
