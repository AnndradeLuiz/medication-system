package com.luiz.medication_system.dominio;

import java.util.Objects;

public class DispensationItem {

    private String medicationId;
    private String medicationName;
    private String concentration;
    private String lotCode;
    private Integer quantity;

    public DispensationItem() {
    }

    public DispensationItem(String medicationId, String medicationName, String concentration, String lotCode, Integer quantity) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.concentration = concentration;
        this.lotCode = lotCode;
        this.quantity = quantity;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DispensationItem item)) return false;
        return Objects.equals(getMedicationId(), item.getMedicationId()) && Objects.equals(getLotCode(), item.getLotCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicationId(), getLotCode());
    }

}
