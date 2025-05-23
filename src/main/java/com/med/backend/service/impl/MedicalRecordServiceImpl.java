package com.med.backend.service.impl;

import com.med.backend.dto.medicalrecord.MedicalRecordDetailsDTO;
import com.med.backend.dto.medicalrecord.SaveMedicalRecordDTO;
import com.med.backend.dto.medicalrecord.UpdateMedicalRecordDTO;
import com.med.backend.exception.DuplicateResourceException;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.Consult;
import com.med.backend.persistence.entity.MedicalNote;
import com.med.backend.persistence.entity.MedicalRecord;
import com.med.backend.persistence.repository.MedicalRecordRepository;
import com.med.backend.persistence.repository.PatientRepository;
import com.med.backend.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalNoteServiceImpl medicalNoteService;

    @Autowired
    private ConsultServiceImpl consultService;

    private static int lastUsedMedicalRecordId = 0;

    @Override
    public MedicalRecord registerOneMedicalRecord(SaveMedicalRecordDTO newMedicalRecord) {
        validatePatientExistence(newMedicalRecord.getPatientId());

        // Verificar si el paciente ya tiene un registro médico
        if (medicalRecordRepository.findByPatientId(newMedicalRecord.getPatientId()).isPresent()) {
            throw new DuplicateResourceException(
                    "MedicalRecord",
                    "patientId",
                    String.valueOf(newMedicalRecord.getPatientId()),
                    "The patient already has a medical record."
            );
        }

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setId(autoIncrement());
        medicalRecord.setAllergies(newMedicalRecord.getAllergies());
        medicalRecord.setChronicConditions(newMedicalRecord.getChronicConditions());
        medicalRecord.setMedications(newMedicalRecord.getMedications());
        medicalRecord.setBloodType(newMedicalRecord.getBloodType());
        medicalRecord.setFamilyHistory(newMedicalRecord.getFamilyHistory());
        medicalRecord.setHeight(newMedicalRecord.getHeight());
        medicalRecord.setWeight(newMedicalRecord.getWeight());
        medicalRecord.setVaccinationHistory(newMedicalRecord.getVaccinationHistory());
        medicalRecord.setPatientId(newMedicalRecord.getPatientId());

        return medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecordDetailsDTO getMedicalRecordDetails(int medicalRecordId) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical record not found for the given medicalRecordId"));

        // Obtener notas médicas y consultas asociadas
        List<MedicalNote> medicalNotes = medicalNoteService.findMedicalNotesByMedicalRecordId(medicalRecordId);
        List<Consult> consults = consultService.findConsultsByMedicalRecordId(medicalRecordId);

        // Construir y devolver el DTO
        return new MedicalRecordDetailsDTO(
                medicalRecord.getId(),
                medicalRecord.getAllergies(),
                medicalRecord.getChronicConditions(),
                medicalRecord.getMedications(),
                medicalRecord.getBloodType(),
                medicalRecord.getFamilyHistory(),
                medicalRecord.getHeight(),
                medicalRecord.getWeight(),
                medicalRecord.getVaccinationHistory(),
                medicalRecord.getPatientId(),
                medicalNotes,
                consults
        );
    }

    @Override
    public MedicalRecord updateMedicalRecord(int medicalRecordId, UpdateMedicalRecordDTO updatedMedicalRecord) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical record not found"));

        if (updatedMedicalRecord.getAllergies() != null) {
            medicalRecord.setAllergies(updatedMedicalRecord.getAllergies());
        }
        if (updatedMedicalRecord.getChronicConditions() != null) {
            medicalRecord.setChronicConditions(updatedMedicalRecord.getChronicConditions());
        }
        if (updatedMedicalRecord.getMedications() != null) {
            medicalRecord.setMedications(updatedMedicalRecord.getMedications());
        }
        if (updatedMedicalRecord.getBloodType() != null) {
            medicalRecord.setBloodType(updatedMedicalRecord.getBloodType());
        }
        if (updatedMedicalRecord.getFamilyHistory() != null) {
            medicalRecord.setFamilyHistory(updatedMedicalRecord.getFamilyHistory());
        }
        if (updatedMedicalRecord.getHeight() != null) {
            medicalRecord.setHeight(updatedMedicalRecord.getHeight());
        }
        if (updatedMedicalRecord.getWeight() != null) {
            medicalRecord.setWeight(updatedMedicalRecord.getWeight());
        }
        if (updatedMedicalRecord.getVaccinationHistory() != null) {
            medicalRecord.setVaccinationHistory(updatedMedicalRecord.getVaccinationHistory());
        }
        if (updatedMedicalRecord.getPatientId() != null && !updatedMedicalRecord.getPatientId().equals(medicalRecord.getPatientId())) {
            validatePatientExistence(updatedMedicalRecord.getPatientId());
            medicalRecord.setPatientId(updatedMedicalRecord.getPatientId());
        }

        return medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public void deleteMedicalRecord(int medicalRecordId) {
        MedicalRecord medicalRecord = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical record not found"));
        medicalRecordRepository.deleteById(medicalRecordId);
    }

    @Override
    public MedicalRecord findMedicalRecordByPatient(int patientId) {
        validatePatientExistence(patientId);
        return medicalRecordRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical record not found for the given patientId"));
    }

    @Override
    public MedicalRecord findMedicalRecordById(int medicalRecordId){
        return medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical record not found for the given medicalRecordId"));
    }

    private int autoIncrement() {
        if (lastUsedMedicalRecordId == 0) {
            lastUsedMedicalRecordId = medicalRecordRepository.findAll().stream()
                    .map(MedicalRecord::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }
        lastUsedMedicalRecordId++;
        return lastUsedMedicalRecordId;
    }

    private void validatePatientExistence(int patientId) {
        if (patientRepository.findById(patientId).isEmpty()) {
            throw new ObjectNotFoundException("Patient not found for the given patientId: " + patientId);
        }
    }
}
