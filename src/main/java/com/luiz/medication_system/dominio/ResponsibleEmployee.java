package com.luiz.medication_system.dominio;

import com.luiz.medication_system.dominio.enums.Role;

import java.util.Objects;

public class ResponsibleEmployee {

    private String employeeId;
    private String name;
    private String registration;

    public ResponsibleEmployee() {
    }

    public ResponsibleEmployee(String employeeId, String name, String registration) {
        this.employeeId = employeeId;
        this.name = name;
        this.registration = registration;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResponsibleEmployee that)) return false;
        return Objects.equals(getEmployeeId(), that.getEmployeeId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEmployeeId());
    }

}
