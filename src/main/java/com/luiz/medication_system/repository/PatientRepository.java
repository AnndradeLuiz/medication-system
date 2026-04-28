package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public interface PatientRepository extends MongoRepository<Patient, String> {

    Optional<Patient> findByCpf(String cpf);
    Optional<Patient> findByCns(String cns);

    List<Patient> findByStatusTrue();
    List<Patient> findByStatusFalse();

    @Query("{'status':  true, $or: [ {'name': { $regex: ?0, $options: 'i' } }, { 'cpf': ?0 }, { 'cns': ?0 } ] }")
    List<Patient> searchActiveByKeyword(String keyword);

    @Query("{'status':  false, $or:  [{'name': {$regex:  ?0, $options:  'i'}}, {'cpf':  ?0}, {'cns':  ?0}]}")
    List<Patient> searchInactiveByKeyword(String keyword);

    @Query("{ $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'cpf': ?0 }, { 'cns': ?0 } ] }")
    List<Patient> searchAllByKeyword(String keyword);

}
