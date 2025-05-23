package com.med.backend.persistence.repository;

import com.med.backend.persistence.entity.MedicalNote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicalNoteRepository extends MongoRepository<MedicalNote,Integer> {
    List<MedicalNote> findByMedicalRecordId(int medicalRecordId);
}
