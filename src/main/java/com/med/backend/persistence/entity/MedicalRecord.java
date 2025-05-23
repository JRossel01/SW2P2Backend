package com.med.backend.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medical_records") // Define la colección como "medical_records" en MongoDB
public class MedicalRecord {

    @Id
    private int id;

    private String allergies;
    private String chronicConditions;
    private String medications;
    private String bloodType;
    private String familyHistory;
    private double height;
    private double weight;
    private String vaccinationHistory;

    // Llave foránea para la relación con Patient
    private int patientId;

    public MedicalRecord() {
    }

    public MedicalRecord(int id, String allergies, String chronicConditions, String medications,
                         String bloodType, String familyHistory, double height, double weight,
                         String vaccinationHistory, int patientId) {
        this.id = id;
        this.allergies = allergies;
        this.chronicConditions = chronicConditions;
        this.medications = medications;
        this.bloodType = bloodType;
        this.familyHistory = familyHistory;
        this.height = height;
        this.weight = weight;
        this.vaccinationHistory = vaccinationHistory;
        this.patientId = patientId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getChronicConditions() {
        return chronicConditions;
    }

    public void setChronicConditions(String chronicConditions) {
        this.chronicConditions = chronicConditions;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getVaccinationHistory() {
        return vaccinationHistory;
    }

    public void setVaccinationHistory(String vaccinationHistory) {
        this.vaccinationHistory = vaccinationHistory;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
