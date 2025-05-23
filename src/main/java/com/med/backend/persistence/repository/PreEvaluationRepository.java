package com.med.backend.persistence.repository;

import com.med.backend.persistence.entity.PreEvaluation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PreEvaluationRepository extends MongoRepository<PreEvaluation, Integer> {
    Optional<PreEvaluation> findByAppointmentId(int appointmentId);
}
