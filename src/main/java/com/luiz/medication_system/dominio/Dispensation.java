package com.luiz.medication_system.dominio;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "dispensation")
public class Dispensation {

    @Id
    private String id;
    private Instant moment;
    private ResponsibleEmployee employee;
    private TargetPatient targetPatient;
    private ThirdParties parties;
    private List<DispensationItem> items = new ArrayList<>();

    public Dispensation() {
    }

    public Dispensation(String id, Instant moment, ResponsibleEmployee employee, TargetPatient targetPatient, ThirdParties parties) {
        this.id = id;
        this.moment = moment;
        this.employee = employee;
        this.targetPatient = targetPatient;
        this.parties = parties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public ResponsibleEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(ResponsibleEmployee employee) {
        this.employee = employee;
    }

    public TargetPatient getTargetPatient() {
        return targetPatient;
    }

    public void setTargetPatient(TargetPatient targetPatient) {
        this.targetPatient = targetPatient;
    }

    public ThirdParties getParties() {
        return parties;
    }

    public void setParties(ThirdParties parties) {
        this.parties = parties;
    }

    public List<DispensationItem> getItems() {
        return items;
    }

    public void setItems(List<DispensationItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Dispensation that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
