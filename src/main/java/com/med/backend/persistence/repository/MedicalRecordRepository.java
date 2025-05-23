package com.med.backend.persistence.repository;

import com.med.backend.persistence.entity.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, Integer> {
    Optional<MedicalRecord> findByPatientId(int patientId);

}
