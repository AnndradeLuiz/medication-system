package com.luiz.medication_system.dominio;

import java.util.Objects;

public class TargetPatient {

    private String id;
    private String name;
    private String cpf;
    private String cns;

    public TargetPatient() {
    }

    public TargetPatient(String id, String name, String cpf, String cns) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.cns = cns;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TargetPatient that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
