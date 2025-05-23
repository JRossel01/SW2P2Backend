package com.med.backend.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pre_evaluations") // Define la colección como "pre_evaluations" en MongoDB
public class PreEvaluation {

    @Id
    private int id;

    @Indexed // Se indexa si necesitas buscar frecuentemente por este campo
    private int appointmentId; // Identificador de la cita asociada

    private String symptoms; // Descripción de los síntomas del paciente
    private String potentialDiagnosis; // Posible diagnóstico basado en los síntomas

    // Constructor sin argumentos
    public PreEvaluation() {}

    // Constructor con argumentos
    public PreEvaluation(int id, int appointmentId, String symptoms, String potentialDiagnosis) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.symptoms = symptoms;
        this.potentialDiagnosis = potentialDiagnosis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPotentialDiagnosis() {
        return potentialDiagnosis;
    }

    public void setPotentialDiagnosis(String potentialDiagnosis) {
        this.potentialDiagnosis = potentialDiagnosis;
    }
}
