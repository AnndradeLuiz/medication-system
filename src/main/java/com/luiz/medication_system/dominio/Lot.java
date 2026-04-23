package com.luiz.medication_system.dominio;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Lot implements Serializable {

    private String laboratory;
    private String lotCode;
    private Date expirationDate;
    private Integer quantity;

    public Lot() {
    }

    public Lot(String laboratory, String lotCode, Date expirationDate, Integer quantity) {
        this.laboratory = laboratory;
        this.lotCode = lotCode;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
