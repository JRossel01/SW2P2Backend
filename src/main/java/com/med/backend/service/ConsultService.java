package com.med.backend.service;

import com.med.backend.dto.consult.SaveConsultDTO;
import com.med.backend.dto.consult.UpdateConsultDTO;
import com.med.backend.persistence.entity.Consult;

import java.util.List;

public interface ConsultService {
    Consult registerOneConsult(SaveConsultDTO newConsult);
    List<Consult> getAllConsults();
    Consult updateConsult(int consultId, UpdateConsultDTO updatedConsultData);
    void deleteConsult(int consultId);
    List<Consult> findConsultsByMedicalRecordId(int medicalRecordId);
    List<Consult> findConsultsByDoctor(int doctorId);
    List<Consult> findConsultsByPatient(int patientId);

    Consult findConsultByAppointment(int appointmentId);
}
