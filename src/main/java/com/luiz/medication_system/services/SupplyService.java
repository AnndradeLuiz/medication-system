package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.MedicalSupply;
import com.luiz.medication_system.dto.MedicalSupplyRequestDTO;
import com.luiz.medication_system.repository.SupplyRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SupplyService {

    private final SupplyRepository repository;

    public SupplyService(SupplyRepository repository) {
        this.repository = repository;
    }

    public List<MedicalSupply> findAll() {
        return repository.findAll();
    }

    public MedicalSupply findById(String id) {
        Optional<MedicalSupply> medicalSupply = repository.findById(id);
        return medicalSupply.orElseThrow(() -> new ObjectNotFoundException("Insumos médico não encontrado"));
    }

    public MedicalSupply insert(MedicalSupply medicalSupply) {
        return repository.insert(medicalSupply);
    }

    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(MedicalSupply medicalSupply) {
        MedicalSupply newMedicalSupply = findById(medicalSupply.getId());
        updateData(newMedicalSupply, medicalSupply);
        repository.save(newMedicalSupply);
    }

    public MedicalSupply fromDto(MedicalSupplyRequestDTO medicalDto) {
        MedicalSupply medicalSupply =  new MedicalSupply(
                null,
                medicalDto.name(),
                medicalDto.observation(),
                medicalDto.sigtapCode()
        );
        if (medicalDto.lots() != null) {
            LocalDate today = LocalDate.now();

            List<Lot> lotList = medicalDto.lots().stream()
                    .map(l -> {
                        if (l.expirationDate().isBefore(today)) {
                            throw new IllegalArgumentException("Erro de Segurança: Não é permitido cadastrar o lote '"
                                    + l.lotCode()
                                    + "' com data de validade vencida!"
                            );
                        }
                        return new Lot(l.laboratory(), l.lotCode(), l.expirationDate(), l.quantity());
                    })
                    .toList();

            medicalSupply.getLots().addAll(lotList);
        }
        return medicalSupply;
    }

    private void updateData(MedicalSupply newMedication, MedicalSupply medicalSupply) {
        if (medicalSupply.getName() != null) {
            newMedication.setName(medicalSupply.getName());
        }
        if (medicalSupply.getObservation() != null) {
            newMedication.setObservation(medicalSupply.getObservation());
        }
        if (medicalSupply.getSigtapCode() != null) {
            newMedication.setSigtapCode(medicalSupply.getSigtapCode());
        }
        if (medicalSupply.getLots() != null && !medicalSupply.getLots().isEmpty()) {
            newMedication.setLots(medicalSupply.getLots());
        }
    }

}
