package com.luiz.medication_system.dominio;

import java.io.Serializable;
import java.util.Objects;

public class MedicalSupplyItem implements Serializable {

    private String medicalSupplyId;
    private String name;
    private String lotCode;
    private Integer quantity;

    public MedicalSupplyItem() {
    }

    public MedicalSupplyItem(String medicalSupplyId, String name, String lotCode, Integer quantity) {
        this.medicalSupplyId = medicalSupplyId;
        this.name = name;
        this.lotCode = lotCode;
        this.quantity = quantity;
    }

    public String getMedicalSupplyId() {
        return medicalSupplyId;
    }

    public void setMedicalSupplyId(String medicalSupplyId) {
        this.medicalSupplyId = medicalSupplyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(o instanceof MedicalSupplyItem that)) return false;
        return Objects.equals(getMedicalSupplyId(), that.getMedicalSupplyId()) && Objects.equals(getLotCode(), that.getLotCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicalSupplyId(), getLotCode());
    }

}
