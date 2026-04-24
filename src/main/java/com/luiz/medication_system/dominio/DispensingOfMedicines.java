package com.luiz.medication_system.dominio;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "dispensation")
public class DispensingOfMedicines {

    @Id
    private String id;
    private Instant moment;
    private ResponsibleEmployee employee;
    private TargetPatient targetPatient;
    private ThirdPerson thirdPerson;
    private List<MedicationItem> medications = new ArrayList<>();

    public DispensingOfMedicines() {
    }

    public DispensingOfMedicines(String id, Instant moment, ResponsibleEmployee employee, TargetPatient targetPatient, ThirdPerson thirdPerson) {
        this.id = id;
        this.moment = moment;
        this.employee = employee;
        this.targetPatient = targetPatient;
        this.thirdPerson = thirdPerson;
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

    public ThirdPerson getThirdPerson() {
        return thirdPerson;
    }

    public void setThirdPerson(ThirdPerson thirdPerson) {
        this.thirdPerson = thirdPerson;
    }

    public List<MedicationItem> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationItem> medications) {
        this.medications = medications;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DispensingOfMedicines that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
