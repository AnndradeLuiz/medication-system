package com.luiz.medication_system.dominio;

import java.io.Serializable;

import java.util.Date;
import java.util.Objects;

public class InclusionProgram implements Serializable {

    private String name;
    private Date inclusionDate;
    private Boolean status;

    public InclusionProgram() {
    }

    public InclusionProgram(String name, Date inclusionDate, Boolean status) {
        this.name = name;
        this.inclusionDate = inclusionDate;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InclusionProgram that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

}
