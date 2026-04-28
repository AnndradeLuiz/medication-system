package com.luiz.medication_system.dominio;

import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;

import java.io.Serializable;

import java.util.Date;

public class InclusionProgram implements Serializable {

    private ProgramCategoryEnum name;

    public InclusionProgram() {
    }

    public InclusionProgram(ProgramCategoryEnum name) {
        this.name = name;
    }

    public ProgramCategoryEnum getName() {
        return name;
    }

    public void setName(ProgramCategoryEnum name) {
        this.name = name;
    }

}
