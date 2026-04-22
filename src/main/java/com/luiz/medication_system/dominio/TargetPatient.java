package com.luiz.medication_system.dominio;

import java.util.Objects;

public class TargetPatient {

    private String patientId;
    private String name;
    private String cpf;
    private String cns;

    public TargetPatient() {
    }

    public TargetPatient(String patientId, String name, String cpf, String cns) {
        this.patientId = patientId;
        this.name = name;
        this.cpf = cpf;
        this.cns = cns;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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
        return Objects.equals(getPatientId(), that.getPatientId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPatientId());
    }

}
