package com.med.backend.service.impl;

import com.med.backend.dto.patient.SavePatientDTO;
import com.med.backend.dto.patient.UpdatePatientDTO;
import com.med.backend.dto.patient.PatientUserDTO;
import com.med.backend.dto.user.SaveUser;
import com.med.backend.exception.DuplicateResourceException;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.Patient;
import com.med.backend.persistence.entity.User;
import com.med.backend.persistence.repository.PatientRepository;
import com.med.backend.persistence.util.Role;
import com.med.backend.service.PatientService;
import com.med.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private static int lastUsedPatientId = 0;

    @Override
    public Patient registerOnePatient(SavePatientDTO newPatient, SaveUser newUser) {
        // Validar que el número de teléfono no esté duplicado
        if (patientRepository.findByPhone(newPatient.getPhone()).isPresent()) {
            throw new DuplicateResourceException(
                    "Patient", "phone", newPatient.getPhone(),
                    "The phone number you entered is already taken. Please choose another one"
            );
        }

        // Crear el usuario asociado al paciente
        User createdUser = userService.registerOneCustomer(newUser);

        // Cambiar el rol del usuario a PATIENT
        createdUser.setRole(Role.PATIENT);
        userService.save(createdUser);

        // Crear la entidad Patient con la información proporcionada
        Patient patient = new Patient();
        patient.setId(autoIncrement()); // Genera un ID único para el Patient
        patient.setDateOfBirth(newPatient.getDateOfBirth());
        patient.setGender(newPatient.getGender());
        patient.setPhone(newPatient.getPhone());
        patient.setAddress(newPatient.getAddress());
        patient.setUserId(createdUser.getId()); // Asocia el paciente al usuario creado

        return patientRepository.save(patient);
    }

    @Override
    public List<PatientUserDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll(); // Asumiendo que tienes un repositorio llamado patientRepository
        return patients.stream()
                .map(patient -> {
                    User user = userService.findById(patient.getUserId())
                            .orElseThrow(() -> new ObjectNotFoundException("User not found for patient with ID " + patient.getId()));
                    return new PatientUserDTO(patient, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Patient updatePatient(int patientId, UpdatePatientDTO updatedPatientData, SaveUser updatedUserData) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ObjectNotFoundException("Patient not found"));

        // Actualiza la información del Patient solo si se proporciona un valor
        if (updatedPatientData.getDateOfBirth() != null) {
            patient.setDateOfBirth(updatedPatientData.getDateOfBirth());
        }
        if (updatedPatientData.getGender() != null) {
            patient.setGender(updatedPatientData.getGender());
        }
        if (updatedPatientData.getPhone() != null) {
            patient.setPhone(updatedPatientData.getPhone());
        }
        if (updatedPatientData.getAddress() != null) {
            patient.setAddress(updatedPatientData.getAddress());
        }
        patientRepository.save(patient);

        // Actualiza la información del usuario asociado solo si se proporciona un valor
        User user = userService.findById(patient.getUserId())
                .orElseThrow(() -> new ObjectNotFoundException("User not found for patient with ID " + patient.getId()));

        if (updatedUserData.getName() != null) {
            user.setName(updatedUserData.getName());
        }
        if (updatedUserData.getUsername() != null) {
            user.setUsername(updatedUserData.getUsername());
        }
        if (updatedUserData.getEmail() != null) {
            user.setEmail(updatedUserData.getEmail());
        }
        if (updatedUserData.getPassword() != null && !updatedUserData.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        }
        userService.save(user);

        return patient;
    }

    @Override
    public PatientUserDTO getPatientWithUserById(int patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ObjectNotFoundException("Patient not found with ID " + patientId));

        User user = userService.findById(patient.getUserId())
                .orElseThrow(() -> new ObjectNotFoundException("User not found for patient with ID " + patientId));

        return new PatientUserDTO(patient, user);
    }

    @Override
    public void deletePatient(int patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ObjectNotFoundException("Patient not found"));

        // Primero elimina el Patient
        patientRepository.deleteById(patientId);

        userService.deleteById(patient.getUserId());
    }

    private int autoIncrement() {
        // Solo inicializar lastUsedPatientId al inicio
        if (lastUsedPatientId == 0) {
            lastUsedPatientId = patientRepository.findAll().stream()
                    .map(Patient::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }

        // Incrementa el último ID usado para Patient
        lastUsedPatientId++;
        return lastUsedPatientId;
    }
}
