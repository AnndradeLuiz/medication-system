package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface EmployeeRepository extends MongoRepository<Employee, String> {

}
