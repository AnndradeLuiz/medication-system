package com.luiz.medication_system.dominio;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Lot implements Serializable {

    private String laboratory;
    private String lotCode;
    private LocalDate expirationDate;
    private Integer initialQuantity;
    private Integer currentQuantity;

    public Lot() {
    }

    public Lot(String laboratory, String lotCode, LocalDate expirationDate, Integer receivedQuantity) {
        this.laboratory = laboratory;
        this.lotCode = lotCode;
        this.expirationDate = expirationDate;
        this.initialQuantity = receivedQuantity;
        this.currentQuantity = receivedQuantity;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getInitialQuantity() {
        return initialQuantity;
    }

    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
    }

    public Integer getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(Integer currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Lot lot)) return false;
        return Objects.equals(getLotCode(), lot.getLotCode());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLotCode());
    }

}
