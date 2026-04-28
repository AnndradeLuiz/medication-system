package com.luiz.medication_system.dominio;

import com.luiz.medication_system.dominio.enums.PharmaceuticalFormEnum;
import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;

import java.util.Objects;

public class MedicationItem {

    private String medicationId;
    private String activeIngredient;
    private String concentration;
    private PharmaceuticalFormEnum PharmaceuticalForm;
    private ProgramCategoryEnum programCategoryEnum;
    private String lotCode;
    private Integer quantity;

    public MedicationItem() {
    }

    public MedicationItem(String medicationId, String activeIngredient, String concentration, PharmaceuticalFormEnum pharmaceuticalForm, ProgramCategoryEnum programCategoryEnum, String lotCode, Integer quantity) {
        this.medicationId = medicationId;
        this.activeIngredient = activeIngredient;
        this.concentration = concentration;
        PharmaceuticalForm = pharmaceuticalForm;
        this.programCategoryEnum = programCategoryEnum;
        this.lotCode = lotCode;
        this.quantity = quantity;
    }

    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
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

    public PharmaceuticalFormEnum getPharmaceuticalForm() {
        return PharmaceuticalForm;
    }

    public void setPharmaceuticalForm(PharmaceuticalFormEnum pharmaceuticalForm) {
        PharmaceuticalForm = pharmaceuticalForm;
    }

    public ProgramCategoryEnum getProgramCategoryEnum() {
        return programCategoryEnum;
    }

    public void setProgramCategoryEnum(ProgramCategoryEnum programCategoryEnum) {
        this.programCategoryEnum = programCategoryEnum;
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
        if (!(o instanceof MedicationItem item)) return false;
        return Objects.equals(getMedicationId(), item.getMedicationId()) && Objects.equals(getLotCode(), item.getLotCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedicationId(), getLotCode());
    }

}
