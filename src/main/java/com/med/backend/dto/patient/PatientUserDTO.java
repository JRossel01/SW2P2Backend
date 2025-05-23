package com.med.backend.dto.patient;

import com.med.backend.persistence.entity.Patient;
import com.med.backend.persistence.entity.User;

public class PatientUserDTO {
    private int idPatient;
    private String name;
    private String username;
    private String email;
    private String dateOfBirth;
    private String gender;
    private String phone;
    private String address;
    private int idUser;

    public PatientUserDTO(Patient patient, User user) {
        this.idPatient = patient.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.dateOfBirth = patient.getDateOfBirth();
        this.gender = patient.getGender();
        this.phone = patient.getPhone();
        this.address = patient.getAddress();
        this.idUser = user.getId();
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
