package com.med.backend.controller;

import com.med.backend.dto.medicalrecord.MedicalRecordDetailsDTO;
import com.med.backend.dto.medicalrecord.SaveMedicalRecordDTO;
import com.med.backend.dto.medicalrecord.UpdateMedicalRecordDTO;
import com.med.backend.persistence.entity.MedicalRecord;
import com.med.backend.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RECEPTIONIST','DOCTOR')")
    @MutationMapping(name = "registerMedicalRecord")
    public MedicalRecord registerMedicalRecord(@Argument("medicalRecordInput") @Valid SaveMedicalRecordDTO newMedicalRecord) {
        return medicalRecordService.registerOneMedicalRecord(newMedicalRecord);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RECEPTIONIST')")
    @QueryMapping(name = "getAllMedicalRecords")
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST', 'DOCTOR', 'PATIENT')")
    @QueryMapping(name = "getMedicalRecordByPatient")
    public MedicalRecord getMedicalRecordByPatient(@Argument("patientId") int patientId) {
        return medicalRecordService.findMedicalRecordByPatient(patientId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RECEPTIONIST')")
    @MutationMapping(name = "updateMedicalRecord")
    public MedicalRecord updateMedicalRecord(@Argument("medicalRecordId") int medicalRecordId,
                                             @Argument("medicalRecordInput") @Valid UpdateMedicalRecordDTO updatedMedicalRecord) {
        return medicalRecordService.updateMedicalRecord(medicalRecordId, updatedMedicalRecord);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RECEPTIONIST','DOCTOR','PATIENT')")
    @QueryMapping(name = "getMedicalRecordById")
    public MedicalRecord getMedicalRecordById(@Argument("medicalRecordId") int medicalRecordId) {
        return medicalRecordService.findMedicalRecordById(medicalRecordId);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "deleteMedicalRecord")
    public void deleteMedicalRecord(@Argument("medicalRecordId") int medicalRecordId) {
        medicalRecordService.deleteMedicalRecord(medicalRecordId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RECEPTIONIST','DOCTOR','PATIENT')")
    @QueryMapping(name = "getMedicalRecordDetails")
    public MedicalRecordDetailsDTO getMedicalRecordDetails(@Argument("medicalRecordId") int medicalRecordId) {
        return medicalRecordService.getMedicalRecordDetails(medicalRecordId);
    }
}
