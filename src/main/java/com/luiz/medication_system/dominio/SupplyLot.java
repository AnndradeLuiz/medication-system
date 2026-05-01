package com.luiz.medication_system.dominio;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class SupplyLot implements Serializable {

    private String lotCode;
    private Instant expirationDate;
    private Integer receivedQuantity;

    public SupplyLot() {
    }

    public SupplyLot(String lotCode, Instant expirationDate, Integer receivedQuantity) {
        this.lotCode = lotCode;
        this.expirationDate = expirationDate;
        this.receivedQuantity = receivedQuantity;
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

    public Integer getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(Integer receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SupplyLot lot)) return false;
        return Objects.equals(getLotCode(), lot.getLotCode());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLotCode());
    }

}
