package com.med.backend.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "doctors") // Define la colección como "doctors" en MongoDB
public class Doctor {

    @Id
    private int id;

    private String specialty;
    @Indexed(unique = true)
    private String licenseNumber; // Número de licencia, asegurado como único
    private String phone;

    // Relación con User (llave foránea)
    private int userId;

    // Constructor sin argumentos
    public Doctor() {}

    // Constructor con argumentos
    public Doctor(int id, String specialty, String licenseNumber, String phone, int userId) {
        this.id = id;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
