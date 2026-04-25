package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    @Query("{$or: [ {'cpf':  ?0}, {'registration':  ?0} ] }")
    Optional<Employee> findByCpfOrRegistration(String login);

}
