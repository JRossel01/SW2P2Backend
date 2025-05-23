package com.med.backend.dto.doctor;

import com.med.backend.persistence.entity.Doctor;
import com.med.backend.persistence.entity.User;

public class DoctorUserDTO {
    private int idDoctor;
    private String name;
    private String username;
    private String email;
    private String specialty;
    private String licenseNumber;
    private String phone;
    private int idUser;

    public DoctorUserDTO(Doctor doctor, User user) {
        this.idDoctor = doctor.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.specialty = doctor.getSpecialty();
        this.licenseNumber = doctor.getLicenseNumber();
        this.phone = doctor.getPhone();
        this.idUser = user.getId();
    }

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
}
