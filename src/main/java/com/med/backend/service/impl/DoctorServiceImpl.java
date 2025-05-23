package com.med.backend.service.impl;

import com.med.backend.dto.doctor.DoctorWithScheduleDTO;
import com.med.backend.dto.doctor.UpdateDoctorDTO;
import com.med.backend.dto.doctor.saveDoctorDto;
import com.med.backend.dto.doctor.DoctorUserDTO;
import com.med.backend.dto.schedule.SaveScheduleDTO;
import com.med.backend.dto.user.SaveUser;
import com.med.backend.exception.DuplicateResourceException;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.Doctor;
import com.med.backend.persistence.entity.Patient;
import com.med.backend.persistence.entity.Schedule;
import com.med.backend.persistence.entity.User;
import com.med.backend.persistence.repository.DoctorRepository;
import com.med.backend.persistence.repository.ScheduleRepository;
import com.med.backend.persistence.util.Role;
import com.med.backend.service.DoctorService;
import com.med.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private static int lastUsedDoctorId = 0;

    @Override
    public Doctor registerOneDoctor(saveDoctorDto newDoctor, SaveUser newUser) {
        // Validar que la licencia no esté duplicada
        if (doctorRepository.findByLicenseNumber(newDoctor.getLicenseNumber()).isPresent()) {
            throw new DuplicateResourceException(
                    "Doctor", "licenseNumber", newDoctor.getLicenseNumber(),
                    "The license number you entered is already taken. Please choose another one"
            );
        }

        // Crear el usuario asociado al doctor como paciente inicialmente
        User createdUser = userService.registerOneCustomer(newUser);

        // Cambiar el rol del usuario a DOCTOR
        createdUser.setRole(Role.DOCTOR);
        userService.save(createdUser);

        // Crear la entidad Doctor con la información proporcionada
        Doctor doctor = new Doctor();
        doctor.setId(autoIncrement()); // Genera un ID único para el Doctor
        doctor.setSpecialty(newDoctor.getSpecialty());
        doctor.setLicenseNumber(newDoctor.getLicenseNumber());
        doctor.setPhone(newDoctor.getPhone());
        doctor.setUserId(createdUser.getId()); // Asocia el doctor al usuario creado

        return doctorRepository.save(doctor);
    }

    @Override
    public List<DoctorUserDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll(); // Asumiendo que tienes un repositorio llamado doctorRepository
        return doctors.stream()
                .map(doctor -> {
                    User user = userService.findById(doctor.getUserId())
                            .orElseThrow(() -> new ObjectNotFoundException("User not found for doctor with ID " + doctor.getId()));
                    return new DoctorUserDTO(doctor, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Doctor updateDoctor(int doctorId, UpdateDoctorDTO updatedDoctorData, SaveUser updatedUserData) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ObjectNotFoundException("Doctor not found"));

        // Actualiza la información del Doctor solo si se proporciona un valor
        if (updatedDoctorData.getSpecialty() != null) {
            doctor.setSpecialty(updatedDoctorData.getSpecialty());
        }
        if (updatedDoctorData.getLicenseNumber() != null) {
            doctor.setLicenseNumber(updatedDoctorData.getLicenseNumber());
        }
        if (updatedDoctorData.getPhone() != null) {
            doctor.setPhone(updatedDoctorData.getPhone());
        }
        doctorRepository.save(doctor);

        // Actualiza la información del usuario asociado solo si se proporciona un valor
        User user = userService.findById(doctor.getUserId())
                .orElseThrow(() -> new ObjectNotFoundException("User not found for doctor with ID " + doctor.getId()));

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

        return doctor;
    }

    @Override
    public void deleteDoctor(int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ObjectNotFoundException("Doctor not found"));

        // Primero elimina el Doctor
        doctorRepository.deleteById(doctorId);

        userService.deleteById(doctor.getUserId());
    }

    @Override
    public DoctorUserDTO getDoctorWithUserById(int doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ObjectNotFoundException("Doctor not found with ID " + doctorId));

        User user = userService.findById(doctor.getUserId())
                .orElseThrow(() -> new ObjectNotFoundException("User not found for doctor with ID " + doctorId));

        return new DoctorUserDTO(doctor, user);
    }

    @Override
    public List<DoctorWithScheduleDTO> getAllDoctorsWithSchedules() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctor -> {
                    User user = userService.findById(doctor.getUserId())
                            .orElseThrow(() -> new ObjectNotFoundException("User not found for doctor with ID " + doctor.getId()));
                    DoctorUserDTO doctorUserDTO = new DoctorUserDTO(doctor, user);

                    // Obtener los horarios del doctor
                    List<Schedule> schedules = scheduleRepository.findByDoctorId(doctor.getId());
                    List<SaveScheduleDTO> scheduleDTOs = schedules.stream()
                            .map(schedule -> {
                                SaveScheduleDTO dto = new SaveScheduleDTO();
                                dto.setDayOfWeek(schedule.getDayOfWeek());
                                dto.setStartTime(schedule.getStartTime());
                                dto.setEndTime(schedule.getEndTime());
                                dto.setDoctorId(schedule.getDoctorId());
                                return dto;
                            })
                            .collect(Collectors.toList());

                    return new DoctorWithScheduleDTO(doctorUserDTO, scheduleDTOs);
                })
                .collect(Collectors.toList());
    }

    private int autoIncrement() {
        // Solo inicializar lastUsedDoctorId al inicio
        if (lastUsedDoctorId == 0) {
            lastUsedDoctorId = doctorRepository.findAll().stream()
                    .map(Doctor::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }

        // Incrementa el último ID usado para Doctor
        lastUsedDoctorId++;
        return lastUsedDoctorId;
    }
}
