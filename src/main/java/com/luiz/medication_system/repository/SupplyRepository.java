package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.Supply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface SupplyRepository extends MongoRepository<Supply, String> {

    Optional<Supply> findByNameAndObservation(String name, String observation);

}
