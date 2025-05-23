package com.med.backend.controller;

import com.med.backend.dto.patient.SavePatientDTO;
import com.med.backend.dto.patient.UpdatePatientDTO;
import com.med.backend.dto.patient.PatientUserDTO;
import com.med.backend.dto.user.SaveUser;
import com.med.backend.persistence.entity.Patient;
import com.med.backend.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST')")
    @MutationMapping(name = "registerPatient")
    public Patient registerPatient(@Argument("patientInput") @Valid SavePatientDTO newPatient,
                                   @Argument("userInput") @Valid SaveUser newUser) {
        return patientService.registerOnePatient(newPatient, newUser);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST','DOCTOR')")
    @QueryMapping(name = "getAllPatients")
    public List<PatientUserDTO> getAllPatients() {
        return patientService.getAllPatients();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST','PATIENT','DOCTOR')")
    @QueryMapping(name = "getPatientWithUserById")
    public PatientUserDTO getPatientWithUserById(@Argument("patientId") int patientId) {
        return patientService.getPatientWithUserById(patientId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST')")
    @MutationMapping(name = "updatePatient")
    public Patient updatePatient(@Argument("patientId") int patientId,
                                 @Argument("patientInput") @Valid UpdatePatientDTO updatedPatient,
                                 @Argument("userInput") @Valid SaveUser updatedUser) {
        return patientService.updatePatient(patientId, updatedPatient, updatedUser);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST')")
    @MutationMapping(name = "deletePatient")
    public void deletePatient(@Argument("patientId") int patientId) {
        patientService.deletePatient(patientId);
    }
}
