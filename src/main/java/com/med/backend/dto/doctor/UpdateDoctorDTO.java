package com.med.backend.dto.doctor;

import jakarta.validation.constraints.Size;

public class UpdateDoctorDTO {

    @Size(max = 50, message = "Specialty must be between 3 and 50 characters")
    private String specialty;

    private String licenseNumber; // Opcional

    private String phone; // Opcional

    // Getters y Setters
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
}
