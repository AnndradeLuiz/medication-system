package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.Supply;
import com.luiz.medication_system.dto.LotRequestDTO;
import com.luiz.medication_system.dto.SupplyRequestDTO;
import com.luiz.medication_system.repository.SupplyRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class SupplyService {

    private final SupplyRepository repository;

    public SupplyService(SupplyRepository repository) {
        this.repository = repository;
    }

    public List<Supply> findAll() {
        return repository.findAll();
    }

    public Supply findById(String id) {
        Optional<Supply> medicalSupply = repository.findById(id);
        return medicalSupply.orElseThrow(() -> new ObjectNotFoundException("Insumos médico não encontrado"));
    }

    public Supply insert(Supply supply) {
        validateUniqueness(supply);
        return repository.insert(supply);
    }

    public void deleteById(String id) {
        findById(id);
        repository.deleteById(id);
    }

    public void update(Supply supply) {
        Supply newSupply = findById(supply.getId());
        updateData(newSupply, supply);
        repository.save(newSupply);
    }

    public void addLots(String id, List<LotRequestDTO> lotsDto) {
        Supply supply = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Insumo não encontrado. ID: " + id));
        for (LotRequestDTO dto : lotsDto) {
            Lot newLot = new Lot();
            newLot.setLotCode(dto.lotCode());
            newLot.setExpirationDate(dto.expirationDate());
            newLot.setInitialQuantity(dto.quantity());
            newLot.setCurrentQuantity(dto.quantity());

            supply.getLots().add(newLot);
        }
        repository.save(supply);
    }

    public void validateUniqueness(Supply supply) {
        Optional<Supply> existing = repository.findByNameAndObservation(
                supply.getName(),
                supply.getObservation()
        );
        if (existing.isPresent() && !existing.get().getId().equals(supply.getId())) {
            throw new IllegalArgumentException(
                    "Erro: Já existe um insumo cadastrado com este nome ("
                            + supply.getName() + ") e observação (" + supply.getObservation() +")."
            );
        }
    }

    public Supply fromDto(SupplyRequestDTO medicalDto) {
        Supply supply =  new Supply(
                null,
                medicalDto.name(),
                medicalDto.observation()
        );
        if (medicalDto.lots() != null) {
            Instant today = Instant.now();

            List<Lot> lotList = medicalDto.lots().stream()
                    .map(l -> {
                        if (l.expirationDate().isBefore(today)) {
                            throw new IllegalArgumentException("Erro de Segurança: Não é permitido cadastrar o lote '"
                                    + l.lotCode()
                                    + "' com data de validade vencida!"
                            );
                        }
                        return new Lot(l.lotCode(), l.expirationDate(), l.quantity());
                    })
                    .toList();

            supply.getLots().addAll(lotList);
        }
        return supply;
    }

    private void updateData(Supply newMedication, Supply supply) {
        if (supply.getName() != null) {
            newMedication.setName(supply.getName());
        }
        if (supply.getObservation() != null) {
            newMedication.setObservation(supply.getObservation());
        }
        if (supply.getLots() != null && !supply.getLots().isEmpty()) {
            newMedication.setLots(supply.getLots());
        }
    }

}
