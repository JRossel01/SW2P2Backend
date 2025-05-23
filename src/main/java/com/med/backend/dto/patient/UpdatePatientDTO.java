package com.med.backend.dto.patient;

import jakarta.validation.constraints.Size;

public class UpdatePatientDTO {

    private String dateOfBirth; // Opcional

    private String gender; // Opcional

    @Size(min = 7, max = 15, message = "Phone must be between 10 and 15 characters")
    private String phone; // Opcional

    private String address; // Opcional

    // Getters y Setters
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
