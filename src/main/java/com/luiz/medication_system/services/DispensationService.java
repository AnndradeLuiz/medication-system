package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.*;
import com.luiz.medication_system.dto.DispensationRequestDTO;
import com.luiz.medication_system.repository.DispensationRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DispensationService {

    private final DispensationRepository repository;

    private final PatientService patientService;
    private final EmployeeService employeeService;
    private final MedicationService medicationService;

    public DispensationService(DispensationRepository repository, PatientService patientService, EmployeeService employeeService, MedicationService medicationService) {
        this.repository = repository;
        this.patientService = patientService;
        this.employeeService = employeeService;
        this.medicationService = medicationService;
    }

    public List<Dispensation> findAll() {
        return repository.findAll();
    }

    public Dispensation findById(String id) {
        Optional<Dispensation> entity = repository.findById(id);
        return entity.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public Dispensation insert(Dispensation entity) {
        return repository.insert(entity);
    }

    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(Dispensation entity) {
        Dispensation newEntity = findById(entity.getId());
        updateData(newEntity, entity);
        repository.save(newEntity);
    }

    public Dispensation fromDto(DispensationRequestDTO entityDto) {
        Instant moment = Instant.now();
        Employee employee = employeeService.findById(entityDto.employeeId());
        ResponsibleEmployee responsibleEmployee = new ResponsibleEmployee(
                employee.getId(),
                employee.getName(),
                employee.getRegistration()
        );
        Patient patient = patientService.findById(entityDto.patientId());
        TargetPatient targetPatient = new TargetPatient(
                patient.getId(),
                patient.getName(),
                patient.getCpf(),
                patient.getCns()
        );
        ThirdParties parties = null;
        if (entityDto.parties() != null) {
            parties = new ThirdParties(
                    entityDto.parties().name(),
                    entityDto.parties().document(),
                    entityDto.parties().observation()
            );
        }
        Dispensation dispensation = new Dispensation(
                null,
                moment,
                responsibleEmployee,
                targetPatient,
                parties
        );
        for (var itemDto : entityDto.items()) {
            Medication med = medicationService.findById(itemDto.medicationId());
            int amountNeeded = itemDto.quantity();

            if (med.getTotalStock() < amountNeeded) {
                throw new IllegalArgumentException("Estoque insuficiente para o medicamento: " + med.getName());
            }

            med.getLots().sort(Comparator.comparing(Lot::getExpirationDate));

            for (Lot lot : med.getLots()) {
                if (amountNeeded <= 0) break;
                if (lot.getQuantity() == 0) break;

                int amountToTakeFromThisLot = Math.min(lot.getQuantity(), amountNeeded);
                lot.setQuantity(lot.getQuantity() - amountToTakeFromThisLot);
                amountNeeded -= amountToTakeFromThisLot;
                DispensationItem receiptItem = new DispensationItem(
                        med.getId(),
                        med.getName(),
                        med.getConcentration(),
                        lot.getLotCode(),
                        amountToTakeFromThisLot
                );
                dispensation.getItems().add(receiptItem);
            }
            medicationService.update(med);
        }
        return dispensation;
    }

    private void updateData(Dispensation newEntity, Dispensation entity) {

    }

}
