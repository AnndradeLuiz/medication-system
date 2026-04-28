package com.luiz.medication_system.dominio;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "dispensing-of-supplies")
public class DispensingOfSupply implements Serializable {

    @Id
    private String id;
    private Instant moment;
    private ResponsibleEmployee employee;
    private String observation;
    private List<SupplyItem> supplies = new ArrayList<>();

    public DispensingOfSupply() {
    }

    public DispensingOfSupply(String id, Instant moment, ResponsibleEmployee employee, String observation) {
        this.id = id;
        this.moment = moment;
        this.employee = employee;
        this.observation = observation;
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

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<SupplyItem> getSupplies() {
        return supplies;
    }

    public void setSupplies(List<SupplyItem> supplies) {
        this.supplies = supplies;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DispensingOfSupply supply)) return false;
        return Objects.equals(getId(), supply.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
