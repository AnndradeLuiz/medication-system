package com.luiz.medication_system.dominio;

import com.luiz.medication_system.dominio.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "employee")
public class Employee implements Serializable {

    @Id
    private String id;
    private String name;
    private String cpf;
    private String registration;
    private String password;
    private Role role;
    private Boolean status;

    public Employee() {
    }

    public Employee(String id, String name, String cpf, String registration, String password, Role role, Boolean status) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.registration = registration;
        this.password = password;
        this.role = role;
        this.status = status;
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

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
