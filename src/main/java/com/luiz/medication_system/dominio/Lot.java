package com.luiz.medication_system.dominio;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class Lot implements Serializable {

    private String lotCode;
    private Instant expirationDate;
    private Integer initialQuantity;
    private Integer currentQuantity;

    public Lot() {
    }

    public Lot(String lotCode, Instant expirationDate, Integer receivedQuantity) {
        this.lotCode = lotCode;
        this.expirationDate = expirationDate;
        this.initialQuantity = receivedQuantity;
        this.currentQuantity = receivedQuantity;
    }

    public String getLotCode() {
        return lotCode;
    }

    public void setLotCode(String lotCode) {
        this.lotCode = lotCode;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
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
