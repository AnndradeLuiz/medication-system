package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.Medication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface MedicationRepository extends MongoRepository<Medication, String> {

    @Query("{ '$where': 'this.totalQuantity <= this.minStock' }")
    Long countLowStock();

    @Query("{ '$where': 'this.totalQuantity <= this.minStock' }")
    List<Medication> findLowStockMedications();
}
