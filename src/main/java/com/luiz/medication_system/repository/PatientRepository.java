package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface PatientRepository extends MongoRepository<Patient, String> {

}
