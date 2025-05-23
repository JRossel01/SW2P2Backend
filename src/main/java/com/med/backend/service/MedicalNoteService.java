package com.med.backend.service;

import com.med.backend.dto.medicalnote.SaveMedicalNoteDTO;
import com.med.backend.dto.medicalnote.UpdateMedicalNoteDTO;
import com.med.backend.persistence.entity.MedicalNote;

import java.util.List;

public interface MedicalNoteService {
    MedicalNote registerOneMedicalNote(SaveMedicalNoteDTO newMedicalNote);
    List<MedicalNote> getAllMedicalNotes();
    MedicalNote updateMedicalNote(int medicalNoteId, UpdateMedicalNoteDTO updatedMedicalNote);
    void deleteMedicalNote(int medicalNoteId);
    List<MedicalNote> findMedicalNotesByMedicalRecordId(int medicalRecordId);
    MedicalNote findMedicalNoteById(int medicalNoteId);
}
