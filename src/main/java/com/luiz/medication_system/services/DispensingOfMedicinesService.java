package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.*;
import com.luiz.medication_system.dto.DispensingOfMedicinesRequestDTO;
import com.luiz.medication_system.repository.DispensingOfMedicinesRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
public class DispensingOfMedicinesService {

    private final DispensingOfMedicinesRepository repository;

    private final PatientService patientService;
    private final EmployeeService employeeService;
    private final MedicationService medicationService;

    public DispensingOfMedicinesService(DispensingOfMedicinesRepository repository, PatientService patientService, EmployeeService employeeService, MedicationService medicationService) {
        this.repository = repository;
        this.patientService = patientService;
        this.employeeService = employeeService;
        this.medicationService = medicationService;
    }

    public List<DispensingOfMedicines> findAll() {
        return repository.findAll();
    }

    // CORREÇÃO AQUI: Estava buscando no patientService, alterado para o próprio repository
    public DispensingOfMedicines findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Registro de dispensação não encontrado"));
    }

    public DispensingOfMedicines insert(DispensingOfMedicines entity) {
        return repository.insert(entity);
    }

    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(DispensingOfMedicines entity) {
        DispensingOfMedicines newEntity = findById(entity.getId());
        updateData(newEntity, entity);
        repository.save(newEntity);
    }

    public DispensingOfMedicines fromDto(DispensingOfMedicinesRequestDTO entityDto) {
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
        ThirdPerson parties = null;
        if (entityDto.thirdPerson() != null) {
            parties = new ThirdPerson(
                    entityDto.thirdPerson().name(),
                    entityDto.thirdPerson().document(),
                    entityDto.thirdPerson().observation()
            );
        }
        DispensingOfMedicines dispensingOfMedicines = new DispensingOfMedicines(
                null,
                moment,
                responsibleEmployee,
                targetPatient,
                parties
        );

        for (var itemDto : entityDto.items()) {
            Medication med = medicationService.findById(itemDto.medicationId());
            if (Boolean.TRUE.equals(patient.getExternal())) {
                if (!String.valueOf(med.getProgramCategory()).equals("FARMACIA_BASICA")) {
                    throw new IllegalArgumentException(
                            "Bloqueio de Segurança: O paciente " + patient.getName() +
                                    " é de outra UBS e só tem permissão para retirar medicamentos da FARMÁCIA BÁSICA. " +
                                    "O item '" + med.getActiveIngredient() +" (" + med.getConcentration() + ")" +
                                    "' pertence à categoria " + med.getProgramCategory() + "."
                    );
                }
            }

            int amountNeeded = itemDto.quantity();

            if (med.getTotalStock() < amountNeeded) {
                throw new IllegalArgumentException("Estoque insuficiente para o medicamento: " +
                        med.getActiveIngredient() +" (" + med.getConcentration() + ")");
            }

            med.getLots().sort(Comparator.comparing(Lot::getExpirationDate));

            for (Lot lot : med.getLots()) {
                if (amountNeeded <= 0) break;
                if (lot.getCurrentQuantity() == 0) continue;

                int amountToTakeFromThisLot = Math.min(lot.getCurrentQuantity(), amountNeeded);
                lot.setCurrentQuantity(lot.getCurrentQuantity() - amountToTakeFromThisLot);
                amountNeeded -= amountToTakeFromThisLot;

                MedicationItem receiptItem = new MedicationItem(
                        med.getId(),
                        med.getActiveIngredient(),
                        med.getConcentration(),
                        med.getPharmaceuticalForm(),
                        med.getProgramCategory(),
                        lot.getLotCode(),
                        amountToTakeFromThisLot
                );
                dispensingOfMedicines.getMedications().add(receiptItem);
            }
            medicationService.update(med);
        }
        return dispensingOfMedicines;
    }

    private void updateData(DispensingOfMedicines newEntity, DispensingOfMedicines entity) {

    }

}