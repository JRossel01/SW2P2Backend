package com.med.backend.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "patients") // Define la colección como "patients" en MongoDB
public class Patient {

    @Id
    private int id;

    private String dateOfBirth;
    private String gender;
    @Indexed(unique = true)
    private String phone;
    private String address;

    // Relación con User (llave foránea)
    private int userId;

    // Constructor sin argumentos
    public Patient() {}

    // Constructor con argumentos
    public Patient(int id, String dateOfBirth, String gender, String phone, String address, int userId) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
