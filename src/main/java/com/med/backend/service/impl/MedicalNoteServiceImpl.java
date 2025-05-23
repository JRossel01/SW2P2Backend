package com.med.backend.service.impl;

import com.med.backend.dto.medicalnote.SaveMedicalNoteDTO;
import com.med.backend.dto.medicalnote.UpdateMedicalNoteDTO;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.MedicalNote;
import com.med.backend.persistence.repository.MedicalNoteRepository;
import com.med.backend.persistence.repository.MedicalRecordRepository;
import com.med.backend.service.MedicalNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalNoteServiceImpl implements MedicalNoteService {

    @Autowired
    private MedicalNoteRepository medicalNoteRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    private static int lastUsedMedicalNoteId = 0;

    @Override
    public MedicalNote registerOneMedicalNote(SaveMedicalNoteDTO newMedicalNote) {
        validateMedicalRecordExistence(newMedicalNote.getMedicalRecordId());

        MedicalNote medicalNote = new MedicalNote();
        medicalNote.setId(autoIncrement());
        medicalNote.setNoteType(newMedicalNote.getNoteType());
        medicalNote.setDetails(newMedicalNote.getDetails());
        medicalNote.setDate(newMedicalNote.getDate());
        medicalNote.setMedicalRecordId(newMedicalNote.getMedicalRecordId());

        return medicalNoteRepository.save(medicalNote);
    }

    @Override
    public List<MedicalNote> getAllMedicalNotes() {
        return medicalNoteRepository.findAll();
    }

    @Override
    public MedicalNote updateMedicalNote(int medicalNoteId, UpdateMedicalNoteDTO updatedMedicalNote) {
        MedicalNote medicalNote = medicalNoteRepository.findById(medicalNoteId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical note not found"));

        if (updatedMedicalNote.getNoteType() != null) {
            medicalNote.setNoteType(updatedMedicalNote.getNoteType());
        }
        if (updatedMedicalNote.getDetails() != null) {
            medicalNote.setDetails(updatedMedicalNote.getDetails());
        }
        if (updatedMedicalNote.getDate() != null) {
            medicalNote.setDate(updatedMedicalNote.getDate());
        }
        if (updatedMedicalNote.getMedicalRecordId() != null && !updatedMedicalNote.getMedicalRecordId().equals(medicalNote.getMedicalRecordId())) {
            validateMedicalRecordExistence(updatedMedicalNote.getMedicalRecordId());
            medicalNote.setMedicalRecordId(updatedMedicalNote.getMedicalRecordId());
        }

        return medicalNoteRepository.save(medicalNote);
    }

    @Override
    public void deleteMedicalNote(int medicalNoteId) {
        MedicalNote medicalNote = medicalNoteRepository.findById(medicalNoteId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical note not found"));
        medicalNoteRepository.deleteById(medicalNoteId);
    }

    @Override
    public List<MedicalNote> findMedicalNotesByMedicalRecordId(int medicalRecordId) {
        validateMedicalRecordExistence(medicalRecordId);
        return medicalNoteRepository.findByMedicalRecordId(medicalRecordId);
    }

    @Override
    public MedicalNote findMedicalNoteById(int medicalNoteId) {
        return medicalNoteRepository.findById(medicalNoteId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical note not found"));
    }

    private int autoIncrement() {
        if (lastUsedMedicalNoteId == 0) {
            lastUsedMedicalNoteId = medicalNoteRepository.findAll().stream()
                    .map(MedicalNote::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }
        lastUsedMedicalNoteId++;
        return lastUsedMedicalNoteId;
    }

    private void validateMedicalRecordExistence(int medicalRecordId) {
        if (medicalRecordRepository.findById(medicalRecordId).isEmpty()) {
            throw new ObjectNotFoundException("Medical record not found for the given medicalRecordId: " + medicalRecordId);
        }
    }
}
