package com.med.backend.controller;

import com.med.backend.dto.doctor.DoctorWithScheduleDTO;
import com.med.backend.dto.doctor.UpdateDoctorDTO;
import com.med.backend.dto.doctor.saveDoctorDto;
import com.med.backend.dto.doctor.DoctorUserDTO;
import com.med.backend.dto.user.SaveUser;
import com.med.backend.persistence.entity.Doctor;
import com.med.backend.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "registerDoctor")
    public Doctor registerDoctor(@Argument("doctorInput") @Valid saveDoctorDto newDoctor,
                                 @Argument("userInput") @Valid SaveUser newUser) {
        return doctorService.registerOneDoctor(newDoctor, newUser);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RECEPTIONIST')")
    @QueryMapping(name = "getAllDoctors")
    public List<DoctorUserDTO> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "updateDoctor")
    public Doctor updateDoctor(@Argument("doctorId") int doctorId,
                               @Argument("doctorInput") @Valid UpdateDoctorDTO updatedDoctor,
                               @Argument("userInput") @Valid SaveUser updatedUser) {
        return doctorService.updateDoctor(doctorId, updatedDoctor, updatedUser);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST','PATIENT','DOCTOR')")
    @QueryMapping(name = "getDoctorWithUserById")
    public DoctorUserDTO getDoctorWithUserById(@Argument("doctorId") int doctorId) {
        return doctorService.getDoctorWithUserById(doctorId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','RECEPTIONIST','PATIENT')")
    @QueryMapping(name = "getAllDoctorsWithSchedules")
    public List<DoctorWithScheduleDTO> getAllDoctorsWithSchedules() {
        return doctorService.getAllDoctorsWithSchedules();
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "deleteDoctor")
    public void deleteDoctor(@Argument("doctorId") int doctorId) {
        doctorService.deleteDoctor(doctorId);
    }
}
