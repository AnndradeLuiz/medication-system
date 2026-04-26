package com.luiz.medication_system.repository;

import com.luiz.medication_system.dominio.DispensingOfMedicines;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface DispensingOfMedicinesRepository extends MongoRepository<DispensingOfMedicines, String> {

    Long countByMomentBetween(Instant start, Instant end);

    // Agregação para achar o nome do remédio mais comum hoje
    @Aggregation(pipeline = {
            "{ $match: { moment: { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { _id: '$items.medicationName', count: { $sum: 1 } } }",
            "{ $sort: { count: -1 } }",
            "{ $limit: 1 }"
    })
    String findMostDispensedToday(Instant start, Instant end);

}
