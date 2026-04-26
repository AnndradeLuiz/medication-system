package com.luiz.medication_system.resources;

import com.luiz.medication_system.dto.DashboardDTO;
import com.luiz.medication_system.dto.LowStockAlertDTO;
import com.luiz.medication_system.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardResource {

    private final DashboardService service;

    public DashboardResource(DashboardService service) {
        this.service = service;
    }

    @GetMapping(value = "/metrics")
    public ResponseEntity<DashboardDTO> getMetrics() {
        DashboardDTO metrics = service.getMetrics();
        return ResponseEntity.ok().body(metrics);
    }

    @GetMapping(value = "/low-stock-alerts")
    public ResponseEntity<List<LowStockAlertDTO>> getLowStockAlerts() {
        return ResponseEntity.ok().body(service.getLowStockAlerts());
    }

}