package com.med.backend.service;

import java.util.List;
import com.med.backend.dto.patient.SavePatientDTO;
import com.med.backend.dto.patient.UpdatePatientDTO;
import com.med.backend.dto.patient.PatientUserDTO;
import com.med.backend.dto.user.SaveUser;
import com.med.backend.persistence.entity.Patient;

public interface PatientService {
    Patient registerOnePatient(SavePatientDTO newPatient, SaveUser newUser);
    List<PatientUserDTO> getAllPatients();
    Patient updatePatient(int patientId, UpdatePatientDTO updatedPatientData, SaveUser updatedUserData);
    void deletePatient(int patientId);
    PatientUserDTO getPatientWithUserById(int patientId);

}
