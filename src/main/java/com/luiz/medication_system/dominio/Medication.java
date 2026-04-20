package com.luiz.medication_system.dominio;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "medication")
public class Medication implements Serializable {

    @Id
    private String id;
    private String name;

    private String category;

    private String pharmaceuticalForm;
    private String unitOfMeasurement;

    private String activeIngredient;
    private String concentration;
    private String sigtapCode;

    private List<Lot> lots = new ArrayList<>();

    public Medication() {
    }

    public Medication(String id, String name, String category, String pharmaceuticalForm, String unitOfMeasurement, String activeIngredient,  String concentration, String sigtapCode) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.pharmaceuticalForm = pharmaceuticalForm;
        this.unitOfMeasurement = unitOfMeasurement;
        this.activeIngredient = activeIngredient;
        this.concentration = concentration;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPharmaceuticalForm() {
        return pharmaceuticalForm;
    }

    public void setPharmaceuticalForm(String pharmaceuticalForm) {
        this.pharmaceuticalForm = pharmaceuticalForm;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public String getSigtapCode() {
        return sigtapCode;
    }

    public void setSigtapCode(String sigtapCode) {
        this.sigtapCode = sigtapCode;
    }

    public List<Lot> getLots() {
        return lots;
    }

    public void setLots(List<Lot> lots) {
        this.lots = lots;
    }

    public Integer getTotalStock() {
        if (lots == null || lots.isEmpty()) {
            return 0;
        }
        return lots.stream().mapToInt(Lot::getQuantity).sum();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Medication that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
