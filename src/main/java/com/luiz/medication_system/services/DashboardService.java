package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.Medication;
import com.luiz.medication_system.dto.DashboardDTO;
import com.luiz.medication_system.dto.LowStockAlertDTO;
import com.luiz.medication_system.repository.DispensingOfMedicinesRepository;
import com.luiz.medication_system.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class DashboardService {

    private final DispensingOfMedicinesRepository dispensingRepository;
    private final MedicationRepository medicationRepository;

    public DashboardService(DispensingOfMedicinesRepository dispensingRepository, MedicationRepository medicationRepository) {
        this.dispensingRepository = dispensingRepository;
        this.medicationRepository = medicationRepository;
    }

    public DashboardDTO getMetrics() {
        ZoneId zone = ZoneId.systemDefault();
        LocalDate hoje = LocalDate.now(zone);

        Instant startOfDay = hoje.atStartOfDay(zone).toInstant();
        Instant endOfDay = hoje.atTime(LocalTime.MAX).atZone(zone).toInstant();

        Long todayCount = dispensingRepository.countByMomentBetween(startOfDay, endOfDay);
        String topItem = dispensingRepository.findMostDispensedToday(startOfDay, endOfDay);

        List<Medication> allMeds = medicationRepository.findAll();

        Long lowStock = allMeds.stream()
                .filter(med -> {
                    int total = med.getTotalStock() != null ? med.getTotalStock() : 0;
                    return total <= 50;
                })
                .count();

        Instant limitDate = Instant.now().plus(30, ChronoUnit.DAYS);

        Long expiringCount = allMeds.stream()
                .filter(med -> med.getLots() != null)
                .flatMap(med -> med.getLots().stream())
                .filter(lot -> lot.getExpirationDate() != null && !lot.getExpirationDate().isAfter(limitDate))
                .count();

        return new DashboardDTO(todayCount, lowStock, expiringCount, topItem != null ? topItem : "Nenhum hoje");
    }

    public List<LowStockAlertDTO> getLowStockAlerts() {
        List<Medication> allMeds = medicationRepository.findAll();

        return allMeds.stream()
                .filter(med -> {
                    int total = med.getTotalStock() != null ? med.getTotalStock() : 0;
                    int limiteMinimo = 50;

                    return total <= limiteMinimo;
                })
                .map(med -> {
                    int total = med.getTotalStock() != null ? med.getTotalStock() : 0;
                    int limiteMinimo = 50;

                    String status = "Atenção";
                    if (total <= (limiteMinimo / 2)) {
                        status = "Crítico";
                    }

                    String validade = "Sem lote";
                    if (med.getLots() != null && !med.getLots().isEmpty()) {
                        Lot closestLot = med.getLots().stream()
                                .min(Comparator.comparing(Lot::getExpirationDate))
                                .orElse(null);

                        if (closestLot != null && closestLot.getExpirationDate() != null) {
                            validade = closestLot.getExpirationDate().toString();
                        }
                    }

                    return new LowStockAlertDTO(
                            med.getActiveIngredient(),
                            med.getConcentration(),
                            total,
                            validade,
                            status
                    );
                })
                .sorted(Comparator.comparing(LowStockAlertDTO::currentQuantity))
                .toList();
    }

}

