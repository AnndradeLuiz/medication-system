package com.luiz.medication_system.dominio;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class Lot implements Serializable {

    private String lotCode;
    private Integer quantity;
    private Instant expirationDate;

    public Lot() {
    }

    public Lot(String lotCode, Integer quantity, Instant expirationDate) {
        this.lotCode = lotCode;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
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

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
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
