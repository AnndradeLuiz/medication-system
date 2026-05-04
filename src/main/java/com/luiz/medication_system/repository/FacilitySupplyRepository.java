package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.FacilitySupply;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilitySupplyRepository extends MongoRepository<FacilitySupply, String> {
}
