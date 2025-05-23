package com.med.backend.service;

import com.med.backend.dto.medicalrecord.MedicalRecordDetailsDTO;
import com.med.backend.dto.medicalrecord.SaveMedicalRecordDTO;
import com.med.backend.dto.medicalrecord.UpdateMedicalRecordDTO;
import com.med.backend.persistence.entity.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecord registerOneMedicalRecord(SaveMedicalRecordDTO newMedicalRecord);
    List<MedicalRecord> getAllMedicalRecords();
    MedicalRecord updateMedicalRecord(int medicalRecordId, UpdateMedicalRecordDTO updatedMedicalRecord);
    void deleteMedicalRecord(int medicalRecordId);
    MedicalRecord findMedicalRecordByPatient(int patientId);
    MedicalRecord findMedicalRecordById(int medicalRecordId);

    MedicalRecordDetailsDTO getMedicalRecordDetails(int medicalRecordId);

}
