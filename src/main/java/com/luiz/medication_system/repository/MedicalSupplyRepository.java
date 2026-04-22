package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.MedicalSupply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface MedicalSupplyRepository extends MongoRepository<MedicalSupply, String> {

}
