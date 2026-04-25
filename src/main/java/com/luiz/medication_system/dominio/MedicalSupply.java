package com.luiz.medication_system.dominio;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "medical-supply")
public class MedicalSupply implements Serializable {

    @Id
    private String id;
    private String name;
    private String observation;
    private String sigtapCode;
    private List<Lot> lots = new ArrayList<>();

    public MedicalSupply() {
    }

    public MedicalSupply(String id, String name, String observation, String sigtapCode) {
        this.id = id;
        this.name = name;
        this.observation = observation;
        this.sigtapCode = sigtapCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<Lot> getLots() {
        return lots;
    }

    public void setLots(List<Lot> lots) {
        this.lots = lots;
    }

    public String getSigtapCode() {
        return sigtapCode;
    }

    public void setSigtapCode(String sigtapCode) {
        this.sigtapCode = sigtapCode;
    }

    public Integer getTotalStock() {
        if (this.lots == null || this.lots.isEmpty()) {
            return 0;
        }
        return this.lots.stream()
                .mapToInt(Lot::getCurrentQuantity)
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MedicalSupply that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
