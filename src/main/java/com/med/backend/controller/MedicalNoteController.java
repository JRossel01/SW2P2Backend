package com.med.backend.controller;

import com.med.backend.dto.medicalnote.SaveMedicalNoteDTO;
import com.med.backend.dto.medicalnote.UpdateMedicalNoteDTO;
import com.med.backend.persistence.entity.MedicalNote;
import com.med.backend.service.MedicalNoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MedicalNoteController {

    @Autowired
    private MedicalNoteService medicalNoteService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'DOCTOR')")
    @MutationMapping(name = "registerMedicalNote")
    public MedicalNote registerMedicalNote(@Argument("medicalNoteInput") @Valid SaveMedicalNoteDTO newMedicalNote) {
        return medicalNoteService.registerOneMedicalNote(newMedicalNote);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @QueryMapping(name = "getAllMedicalNotes")
    public List<MedicalNote> getAllMedicalNotes() {
        return medicalNoteService.getAllMedicalNotes();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'DOCTOR','RECEPTIONIST')")
    @QueryMapping(name = "getMedicalNotesByMedicalRecord")
    public List<MedicalNote> getMedicalNotesByMedicalRecord(@Argument("medicalRecordId") int medicalRecordId) {
        return medicalNoteService.findMedicalNotesByMedicalRecordId(medicalRecordId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @MutationMapping(name = "updateMedicalNote")
    public MedicalNote updateMedicalNote(@Argument("medicalNoteId") int medicalNoteId,
                                         @Argument("medicalNoteInput") @Valid UpdateMedicalNoteDTO updatedMedicalNote) {
        return medicalNoteService.updateMedicalNote(medicalNoteId, updatedMedicalNote);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @QueryMapping(name = "getMedicalNoteById")
    public MedicalNote getMedicalNoteById(@Argument("medicalNoteId") int medicalNoteId) {
        return medicalNoteService.findMedicalNoteById(medicalNoteId);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "deleteMedicalNote")
    public void deleteMedicalNote(@Argument("medicalNoteId") int medicalNoteId) {
        medicalNoteService.deleteMedicalNote(medicalNoteId);
    }
}
