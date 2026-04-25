package com.luiz.medication_system.dominio;

import com.luiz.medication_system.dominio.enums.AdministrationRouteEnum;
import com.luiz.medication_system.dominio.enums.PharmaceuticalFormEnum;
import com.luiz.medication_system.dominio.enums.ProgramCategoryEnum;
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
    private String activeIngredient;
    private String concentration;

    private PharmaceuticalFormEnum PharmaceuticalForm;
    private AdministrationRouteEnum administrationRoute;

    private ProgramCategoryEnum programCategoryEnum;
    private String sigtapCode;

    private List<Lot> lots = new ArrayList<>();

    public Medication() {
    }

    public Medication(String id, String name, String activeIngredient, String concentration, PharmaceuticalFormEnum pharmaceuticalForm, AdministrationRouteEnum administrationRoute, ProgramCategoryEnum programCategoryEnum, String sigtapCode) {
        this.id = id;
        this.name = name;
        this.activeIngredient = activeIngredient;
        this.concentration = concentration;
        PharmaceuticalForm = pharmaceuticalForm;
        this.administrationRoute = administrationRoute;
        this.programCategoryEnum = programCategoryEnum;
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

    public AdministrationRouteEnum getAdministrationRoute() {
        return administrationRoute;
    }

    public void setAdministrationRoute(AdministrationRouteEnum administrationRoute) {
        this.administrationRoute = administrationRoute;
    }

    public ProgramCategoryEnum getProgramCategory() {
        return programCategoryEnum;
    }

    public void setProgramCategory(ProgramCategoryEnum programCategoryEnum) {
        this.programCategoryEnum = programCategoryEnum;
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
        if (this.lots == null || this.lots.isEmpty()) {
            return 0;
        }
        return this.lots.stream()
                .mapToInt(Lot::getCurrentQuantity)
                .sum();
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
