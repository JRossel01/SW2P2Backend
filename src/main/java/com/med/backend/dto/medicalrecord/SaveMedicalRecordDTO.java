package com.med.backend.dto.medicalrecord;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SaveMedicalRecordDTO {

    @NotBlank(message = "Allergies are required")
    private String allergies;

    @NotBlank(message = "Chronic conditions are required")
    private String chronicConditions;

    @NotBlank(message = "Medications are required")
    private String medications;

    @NotBlank(message = "Blood type is required")
    private String bloodType;

    @NotBlank(message = "Family history is required")
    private String familyHistory;

    @Positive(message = "Height must be a positive number")
    private double height;

    @Positive(message = "Weight must be a positive number")
    private double weight;

    @NotBlank(message = "Vaccination history is required")
    private String vaccinationHistory;

    @NotNull(message = "Patient ID is required")
    private Integer patientId;

    // Getters and Setters
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

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}
