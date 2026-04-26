package com.luiz.medication_system.services;

import com.luiz.medication_system.dominio.Lot;
import com.luiz.medication_system.dominio.Medication;
import com.luiz.medication_system.dto.DashboardDTO;
import com.luiz.medication_system.dto.LowStockAlertDTO;
import com.luiz.medication_system.repository.DispensingOfMedicinesRepository;
import com.luiz.medication_system.repository.MedicationRepository;
import org.springframework.stereotype.Service;

import java.time.*;
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
        LocalDateTime startOfDayLocal = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDayLocal = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        ZoneId zone = ZoneId.systemDefault();
        Instant startOfDay = startOfDayLocal.atZone(zone).toInstant();
        Instant endOfDay = endOfDayLocal.atZone(zone).toInstant();

        // 1. Busca no Banco as saídas de hoje
        Long todayCount = dispensingRepository.countByMomentBetween(startOfDay, endOfDay);
        String topItem = dispensingRepository.findMostDispensedToday(startOfDay, endOfDay);

        // 2. Trazemos todos os medicamentos para o Java fazer as contas
        List<Medication> allMeds = medicationRepository.findAll();

        // 3. Conta quantos medicamentos estão com estoque baixo (regra dos 50)
        Long lowStock = allMeds.stream()
                .filter(med -> {
                    int total = med.getTotalStock() != null ? med.getTotalStock() : 0;
                    return total <= 50;
                })
                .count();

        // 4. Conta quantos lotes estão vencendo em até 30 dias
        LocalDate limitDate = LocalDate.now().plusDays(30);
        Long expiringCount = allMeds.stream()
                .filter(med -> med.getLots() != null) // Pega apenas os que têm lotes
                .flatMap(med -> med.getLots().stream()) // Despeja todos os lotes de todos os remédios numa "esteira" só
                .filter(lot -> lot.getExpirationDate() != null && !lot.getExpirationDate().isAfter(limitDate))
                .count();

        return new DashboardDTO(todayCount, lowStock, expiringCount, topItem != null ? topItem : "Nenhum hoje");
    }

    public List<LowStockAlertDTO> getLowStockAlerts() {
        // 1. Como a conta é feita no Java, buscamos todos os medicamentos ativos
        List<Medication> allMeds = medicationRepository.findAll();

        return allMeds.stream()
                // 2. FILTRO DE ESTOQUE BAIXO: Só deixa passar quem tem estoque menor que a regra
                .filter(med -> {
                    int total = med.getTotalStock() != null ? med.getTotalStock() : 0;

                    // REGRA PROVISÓRIA: Se você não tem minStock na classe, vamos assumir que
                    // qualquer remédio com menos de 50 unidades está acabando.
                    // O ideal no futuro é adicionar um "Integer minStock" na classe Medication!
                    int limiteMinimo = 50;

                    return total <= limiteMinimo;
                })
                // 3. MAP: Transforma os que passaram no filtro em linhas da tabela (DTO)
                .map(med -> {
                    int total = med.getTotalStock() != null ? med.getTotalStock() : 0;
                    int limiteMinimo = 50; // Usando o mesmo limite provisório

                    // Lógica de Status: Se tiver menos da metade do mínimo, é Crítico
                    String status = "Atenção";
                    if (total <= (limiteMinimo / 2)) {
                        status = "Crítico";
                    }

                    // Lógica de Validade: Procura o lote que vence primeiro
                    String validade = "Sem lote";
                    if (med.getLots() != null && !med.getLots().isEmpty()) {
                        Lot closestLot = med.getLots().stream()
                                .min(java.util.Comparator.comparing(Lot::getExpirationDate))
                                .orElse(null);

                        if (closestLot != null && closestLot.getExpirationDate() != null) {
                            validade = closestLot.getExpirationDate().toString();
                        }
                    }

                    return new LowStockAlertDTO(
                            med.getName(), // Adicione + " " + med.getDosage() se tiver dosagem
                            total,
                            validade,
                            status
                    );
                })
                // 4. BÔNUS: Ordena a lista para mostrar os piores casos (Críticos) primeiro
                .sorted(java.util.Comparator.comparing(LowStockAlertDTO::currentQuantity))
                .toList();
    }

}

