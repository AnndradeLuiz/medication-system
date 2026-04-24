package com.luiz.medication_system.dominio;

import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;

import java.io.Serializable;

import java.util.Date;

public class InclusionProgram implements Serializable {

    private ProgramCategoryEnum name;
    private Date inclusionDate;
    private Boolean status;

    public InclusionProgram() {
    }

    public InclusionProgram(ProgramCategoryEnum name, Date inclusionDate, Boolean status) {
        this.name = name;
        this.inclusionDate = inclusionDate;
        this.status = status;
    }

    public ProgramCategoryEnum getName() {
        return name;
    }

    public void setName(ProgramCategoryEnum name) {
        this.name = name;
    }

    public Date getInclusionDate() {
        return inclusionDate;
    }

    public void setInclusionDate(Date inclusionDate) {
        this.inclusionDate = inclusionDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
