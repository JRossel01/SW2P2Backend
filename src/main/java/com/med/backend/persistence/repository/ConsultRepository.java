package com.med.backend.persistence.repository;

import com.med.backend.persistence.entity.Consult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultRepository extends MongoRepository<Consult, Integer> {
    List<Consult> findByMedicalRecordId(int medicalRecordId);

    Optional<Object> findByAppointmentId(Integer appointmentId);
    List<Consult> findByAppointmentIdIn(List<Integer> appointmentIds);
    Optional<Consult> findConsultByAppointmentId(Integer appointmentId);


}
