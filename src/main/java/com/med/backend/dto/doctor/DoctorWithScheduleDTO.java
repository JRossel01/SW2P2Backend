package com.med.backend.dto.doctor;

import com.med.backend.dto.schedule.SaveScheduleDTO;

import java.util.List;

public class DoctorWithScheduleDTO {
    private int idDoctor;
    private String name;
    private String username;
    private String email;
    private String specialty;
    private String licenseNumber;
    private String phone;
    private int idUser;
    private List<SaveScheduleDTO> schedules;

    public DoctorWithScheduleDTO(DoctorUserDTO doctorUserDTO, List<SaveScheduleDTO> schedules) {
        this.idDoctor = doctorUserDTO.getIdDoctor();
        this.name = doctorUserDTO.getName();
        this.username = doctorUserDTO.getUsername();
        this.email = doctorUserDTO.getEmail();
        this.specialty = doctorUserDTO.getSpecialty();
        this.licenseNumber = doctorUserDTO.getLicenseNumber();
        this.phone = doctorUserDTO.getPhone();
        this.idUser = doctorUserDTO.getIdUser();
        this.schedules = schedules;
    }

    // Getters and Setters
    public int getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(int idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public List<SaveScheduleDTO> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<SaveScheduleDTO> schedules) {
        this.schedules = schedules;
    }
}
