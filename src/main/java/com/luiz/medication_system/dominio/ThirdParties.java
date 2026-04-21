package com.luiz.medication_system.dominio;

import java.io.Serializable;
import java.util.Objects;

public class ThirdParties implements Serializable {

    String name;
    String document;
    String observation;

    public ThirdParties() {
    }

    public ThirdParties(String name, String document, String observation) {
        this.name = name;
        this.document = document;
        this.observation = observation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ThirdParties that)) return false;
        return Objects.equals(getDocument(), that.getDocument());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDocument());
    }

}
