package com.luiz.medication_system.dominio;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "patient")
public class Patient implements Serializable {

    @Id
    private String id;
    private String name;
    private String cpf;
    private String cns;
    private LocalDate birthDate;
    private Boolean status;
    private Boolean external;
    private List<String> phones = new ArrayList<>();
    private List<InclusionProgram> programs = new ArrayList<>();

    public Patient() {
    }

    public Patient(String id, String name, String cpf, String cns, LocalDate birthDate, Boolean status, Boolean external) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.cns = cns;
        this.birthDate = birthDate;
        this.status = status;
        this.external = external;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getAge() {
        if (getBirthDate() == null) {
            return null;
        }
        LocalDate hoje = LocalDate.now();
        return Period.between(getBirthDate(), hoje).getYears();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getExternal() {
        return external;
    }

    public void setExternal(Boolean external) {
        this.external = external;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<InclusionProgram> getPrograms() {
        return programs;
    }

    public void setPrograms(List<InclusionProgram> programs) {
        this.programs = programs;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Patient patient)) return false;
        return Objects.equals(getId(), patient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
