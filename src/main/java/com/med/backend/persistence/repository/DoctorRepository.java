package com.med.backend.persistence.repository;

import com.med.backend.persistence.entity.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DoctorRepository extends MongoRepository<Doctor, Integer> {
    Optional<Doctor> findByUserId(int userId);

    Optional<Object> findByLicenseNumber(String licenseNumber);
}
