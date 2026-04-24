package com.luiz.medication_system.dominio;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "patient")
public class Patient implements Serializable {

    @Id
    private String id;
    private String name;
    private String cpf;
    private String cns;
    private Date birthDate;
    private List<String> phones = new ArrayList<>();
    private List<InclusionProgram> programs = new ArrayList<>();

    public Patient() {
    }

    public Patient(String id, String name, String cpf, String cns, Date birthDate) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.cns = cns;
        this.birthDate = birthDate;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
