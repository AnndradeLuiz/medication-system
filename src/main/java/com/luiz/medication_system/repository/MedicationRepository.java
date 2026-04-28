package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.Medication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public interface MedicationRepository extends MongoRepository<Medication, String> {

    Optional<Medication> findByActiveIngredientAndConcentration(String activeIngredient, String concentration);

    @Query("{ '$where': 'this.totalQuantity <= this.minStock' }")
    Long countLowStock();

    @Query("{ '$where': 'this.totalQuantity <= this.minStock' }")
    List<Medication> findLowStockMedications();
}
