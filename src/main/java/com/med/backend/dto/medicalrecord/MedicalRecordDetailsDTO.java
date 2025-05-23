package com.med.backend.dto.medicalrecord;

import com.med.backend.persistence.entity.MedicalNote;
import com.med.backend.persistence.entity.Consult;

import java.util.List;

public class MedicalRecordDetailsDTO {
    private int id;
    private String allergies;
    private String chronicConditions;
    private String medications;
    private String bloodType;
    private String familyHistory;
    private double height;
    private double weight;
    private String vaccinationHistory;
    private int patientId;

    // Relaciones
    private List<MedicalNote> medicalNotes;
    private List<Consult> consults;

    public MedicalRecordDetailsDTO() {
    }

    public MedicalRecordDetailsDTO(int id, String allergies, String chronicConditions, String medications,
                                   String bloodType, String familyHistory, double height, double weight,
                                   String vaccinationHistory, int patientId, List<MedicalNote> medicalNotes,
                                   List<Consult> consults) {
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
        this.medicalNotes = medicalNotes;
        this.consults = consults;
    }

    // Getters y Setters


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

    public List<MedicalNote> getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(List<MedicalNote> medicalNotes) {
        this.medicalNotes = medicalNotes;
    }

    public List<Consult> getConsults() {
        return consults;
    }

    public void setConsults(List<Consult> consults) {
        this.consults = consults;
    }
}
