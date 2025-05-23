package com.med.backend.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "consults") // Define la colección como "consults" en MongoDB
public class Consult {

    @Id
    private int id;

    private String date; // Ahora es String
    private String diagnosis;
    private String treatment;
    private String observations;
    private Double currentWeight;
    private Double currentHeight;
    private String attentionTime; // Nueva propiedad para la hora de atención


    // Llave foránea para la relación con MedicalRecord
    private int medicalRecordId;

    // Llave foránea para la relación con Appointment
    private int appointmentId;

    public Consult() {}

    // Modificar el constructor para incluir el nuevo campo
    public Consult(int id, String date, String diagnosis, String treatment, String observations,
                   Double currentWeight, Double currentHeight, int medicalRecordId, int appointmentId,
                   String attentionTime) {
        this.id = id;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.observations = observations;
        this.currentWeight = currentWeight;
        this.currentHeight = currentHeight;
        this.medicalRecordId = medicalRecordId;
        this.appointmentId = appointmentId;
        this.attentionTime = attentionTime; // Inicialización del nuevo campo
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(Double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public Double getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(Double currentHeight) {
        this.currentHeight = currentHeight;
    }

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getAttentionTime() {
        return attentionTime;
    }

    public void setAttentionTime(String attentionTime) {
        this.attentionTime = attentionTime;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
}
