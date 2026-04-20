package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.Medication;
import com.luiz.medication_system.dto.MedicationInsertDTO;
import com.luiz.medication_system.repository.MedicationRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository repository;

    public MedicationService(MedicationRepository repository) {
        this.repository = repository;
    }

    public List<Medication> findAll() {
        return repository.findAll();
    }

    public Medication findById(String id) {
        Optional<Medication> medication = repository.findById(id);
        return medication.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public Medication insert(Medication medication) {
        return repository.insert(medication);
    }

    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(Medication medication) {
        Medication newMedication = findById(medication.getId());
        updateData(newMedication, medication);
        repository.save(newMedication);
    }

    public Medication fromDto(MedicationInsertDTO medicationInsertDTO) {
        Medication medication =  new Medication(
                null,
                medicationInsertDTO.name(),
                medicationInsertDTO.category(),
                medicationInsertDTO.pharmaceuticalForm(),
                medicationInsertDTO.unitOfMeasurement(),
                medicationInsertDTO.activeIngredient(),
                medicationInsertDTO.concentration(),
                medicationInsertDTO.sigtapCode()
        );
        if (medicationInsertDTO.lots() != null) {
            Instant today = Instant.now();

            List<Lot> lotList = medicationInsertDTO.lots().stream()
                    .map(l -> {
                        if (l.expirationDate().isBefore(today)) {
                            throw new IllegalArgumentException("Erro de Segurança: Não é permitido cadastrar o lote '" + l.lotCode() + "' com data de validade vencida!");
                        }
                        return new Lot(l.lotCode(), l.quantity(), l.expirationDate());
                    })
                    .toList();

            medication.getLots().addAll(lotList);
        }
        return medication;
    }

    private void updateData(Medication newMedication, Medication medication) {
        newMedication.setName(medication.getName());
        newMedication.setCategory(medication.getCategory());
        newMedication.setPharmaceuticalForm(medication.getPharmaceuticalForm());
        newMedication.setUnitOfMeasurement(medication.getUnitOfMeasurement());
        newMedication.setActiveIngredient(medication.getActiveIngredient());
        newMedication.setConcentration(medication.getConcentration());
        newMedication.setLots(medication.getLots());
    }

}
