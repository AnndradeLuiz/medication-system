package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.Medication;
import com.luiz.medication_system.dto.LotRequestDTO;
import com.luiz.medication_system.dto.MedicationRequestDTO;
import com.luiz.medication_system.repository.MedicationRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
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

    public void addLots(String medicationId, List<LotRequestDTO> newLotsDto) {
        Medication medication = findById(medicationId);
        List<Lot> lotList = newLotsDto.stream()
                .map(dto -> new Lot(dto.laboratory(), dto.lotCode(), dto.expirationDate(), dto.quantity()))
                .toList();
        medication.getLots().addAll(lotList);
        repository.save(medication);
    }

    public Medication fromDto(MedicationRequestDTO medicationRequestDTO) {
        Medication medication =  new Medication(
                null,
                medicationRequestDTO.name(),
                medicationRequestDTO.activeIngredient(),
                medicationRequestDTO.concentration(),
                medicationRequestDTO.pharmaceuticalForm(),
                medicationRequestDTO.administrationRoute(),
                medicationRequestDTO.programCategory(),
                medicationRequestDTO.sigtapCode()
        );
        if (medicationRequestDTO.lots() != null) {
            Date today = Date.from(Instant.now());

            List<Lot> lotList = medicationRequestDTO.lots().stream()
                    .map(l -> {
                        if (l.expirationDate().before(today)) {
                            throw new IllegalArgumentException("Erro de Segurança: Não é permitido cadastrar o lote '"
                                    + l.lotCode()
                                    + "' com data de validade vencida!");
                        }
                        return new Lot(l.laboratory(), l.lotCode(), l.expirationDate(), l.quantity());
                    })
                    .toList();

            medication.getLots().addAll(lotList);
        }
        return medication;
    }

    private void updateData(Medication newMedication, Medication medication) {
        if (medication.getName() != null) {
            newMedication.setName(medication.getName());
        }
        if (medication.getActiveIngredient() != null) {
            newMedication.setActiveIngredient(medication.getActiveIngredient());
        }
        if (medication.getConcentration() != null) {
            newMedication.setConcentration(medication.getConcentration());
        }
        if (medication.getPharmaceuticalForm() != null) {
            newMedication.setPharmaceuticalForm(medication.getPharmaceuticalForm());
        }
        if (medication.getAdministrationRoute() != null) {
            newMedication.setAdministrationRoute(medication.getAdministrationRoute());
        }
        if (medication.getProgramCategory() != null) {
            newMedication.setProgramCategory(medication.getProgramCategory());
        }
        if (medication.getSigtapCode() != null) {
            newMedication.setSigtapCode(medication.getSigtapCode());
        }
        if (medication.getLots() != null && !medication.getLots().isEmpty()) {
            newMedication.setLots(medication.getLots());
        }
    }

}
