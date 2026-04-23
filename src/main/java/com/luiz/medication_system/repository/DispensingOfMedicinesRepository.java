package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.DispensingOfMedicines;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface DispensingOfMedicinesRepository extends MongoRepository<DispensingOfMedicines, String> {

}
