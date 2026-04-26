package com.luiz.medication_system.dto;

/**
 * DTO que consolida as métricas do painel inicial.
 * @param dispensationsToday Total de saídas no dia corrente.
 * @param lowStockCount Quantidade de itens abaixo do estoque de segurança.
 * @param expiringBatchesCount Lotes que vencem nos próximos 60 dias.
 * @param mostDispensedItem Nome do medicamento com maior volume de saída hoje.
 */

public record DashboardDTO(
        Long dispensationsToday,
        Long lowStockCount,
        Long expiringBatchesCount,
        String mostDispensedItem )
{}
