package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.*;
import com.luiz.medication_system.dto.DispensingOfSupplyRequestDTO;
import com.luiz.medication_system.repository.DispensingOfSupplyRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DispensingOfSupplyService {

    private final DispensingOfSupplyRepository repository;
    private final EmployeeService employeeService;
    private final SupplyService supplyService;

    public DispensingOfSupplyService(DispensingOfSupplyRepository repository, EmployeeService employeeService, SupplyService supplyService) {
        this.repository = repository;
        this.employeeService = employeeService;
        this.supplyService = supplyService;
    }

    public List<DispensingOfSupply> findAll() {
        return repository.findAll();
    }

    public DispensingOfSupply findById(String id) {
        Optional<DispensingOfSupply> entity = repository.findById(id);
        return entity.orElseThrow(() -> new ObjectNotFoundException("Insumos médico não encontrado"));
    }

    public DispensingOfSupply insert(DispensingOfSupply entity) {
        return repository.insert(entity);
    }

    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(DispensingOfSupply entity) {
        DispensingOfSupply newEntity = findById(entity.getId());
        updateData(newEntity, entity);
        repository.save(newEntity);
    }

    public DispensingOfSupply fromDto(DispensingOfSupplyRequestDTO entityDto) {
        Instant moment = Instant.now();
        Employee employee = employeeService.findById(entityDto.employeeId());
        ResponsibleEmployee responsibleEmployee = new ResponsibleEmployee(
                employee.getId(),
                employee.getName(),
                employee.getRegistration()
        );
        DispensingOfSupply supply = new DispensingOfSupply(
                null,
                moment,
                responsibleEmployee,
                entityDto.observation()
        );
        for (var supplyItem : entityDto.supplies()) {
            MedicalSupply medicalSupply = supplyService.findById(supplyItem.medicalSupplyId());
            int amountNeeded = supplyItem.quantity();

            if (medicalSupply.getTotalStock() < amountNeeded) {
                throw new IllegalArgumentException("Estoque insuficiente para o medicamento: " + medicalSupply.getName());
            }

            medicalSupply.getLots().sort(Comparator.comparing(Lot::getExpirationDate));

            for (Lot lot : medicalSupply.getLots()) {
                if (amountNeeded <= 0) break;
                if (lot.getCurrentQuantity() <= 0) continue;

                int amountToTakeFromThisLot = Math.min(lot.getCurrentQuantity(), amountNeeded);
                lot.setCurrentQuantity(lot.getCurrentQuantity() - amountToTakeFromThisLot);
                amountNeeded -= amountToTakeFromThisLot;
                MedicalSupplyItem receiptItem = new MedicalSupplyItem(
                        medicalSupply.getId(),
                        medicalSupply.getName(),
                        lot.getLotCode(),
                        amountToTakeFromThisLot
                );
                supply.getSupplies().add(receiptItem);
            }
            supplyService.update(medicalSupply);
        }
        return supply;
    }

    // Acredito que está função será removida, pois não me faz sentido dar update no registro das saídas
    private void updateData(DispensingOfSupply newMedication, DispensingOfSupply entity) {

    }

}
