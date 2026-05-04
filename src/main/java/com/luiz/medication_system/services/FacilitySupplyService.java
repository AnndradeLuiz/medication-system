package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.FacilitySupply;
import com.luiz.medication_system.dominio.SupplyLot;
import com.luiz.medication_system.dto.FacilitySupplyRequestDTO;
import com.luiz.medication_system.dto.LotRequestDTO;
import com.luiz.medication_system.repository.FacilitySupplyRepository;
import com.luiz.medication_system.services.exceptions.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class FacilitySupplyService {

    private final FacilitySupplyRepository repository;

    public FacilitySupplyService(FacilitySupplyRepository repository) {
        this.repository = repository;
    }

    public FacilitySupply insert(FacilitySupply supply) {
        return repository.insert(supply);
    }

    public List<FacilitySupply> findAll() {
        return repository.findAll();
    }

    public FacilitySupply findById(String id) {
        Optional<FacilitySupply> supply = repository.findById(id);
        return supply.orElseThrow(() -> new ObjectNotFoundException("Funcionário não encontrado."));
    }

    public void update(FacilitySupply supply) {
        FacilitySupply newSupply = findById(supply.getId());
        updateData(newSupply, supply);
        repository.save(newSupply);
    }

    public void addLots(String id, List<LotRequestDTO> lots) {
        FacilitySupply supply = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Funcionário não encontrado."));
        for (LotRequestDTO lotDto : lots) {
            SupplyLot newLot = new SupplyLot();
            newLot.setLotCode(lotDto.lotCode());
            newLot.setExpirationDate(lotDto.expirationDate());
            newLot.setReceivedQuantity(lotDto.quantity());
            supply.getLots().add(newLot);
        }
        repository.save(supply);
    }

    public FacilitySupply fromDto(FacilitySupplyRequestDTO supplyDTO) {
        FacilitySupply supply = new FacilitySupply(
                null,
                supplyDTO.name(),
                supplyDTO.observation()
        );

        if (supplyDTO.lots() != null) {
            Instant today = Instant.now();
            List<SupplyLot> lots = supplyDTO.lots().stream()
                    .map(l -> {
                        if (l.expirationDate().isBefore(today)) {
                            throw new IllegalArgumentException(
                                    "Erro de Segurança: Não é permitido cadastrar o lote '"
                                            + l.lotCode()
                                            + "' com data de validade vencida!"
                            );
                        }
                        return new SupplyLot(l.lotCode(), l.expirationDate(), l.quantity());
                    })
                    .toList();
            supply.getLots().addAll(lots);
        }
        return supply;
    }

    private void updateData(FacilitySupply newSupply, FacilitySupply supply) {
        if (supply.getName() != null) {
            newSupply.setName(supply.getName());
        }
        if (supply.getObservation() != null) {
            newSupply.setObservation(supply.getObservation());
        }
        if (supply.getLots() != null && !supply.getLots().isEmpty()) {
            newSupply.setLots(supply.getLots());
        }
    }
}
