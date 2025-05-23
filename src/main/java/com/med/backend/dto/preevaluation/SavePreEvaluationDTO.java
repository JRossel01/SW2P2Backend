package com.med.backend.dto.preevaluation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SavePreEvaluationDTO {

    @NotNull(message = "Appointment ID is required")
    @Positive(message = "Appointment ID must be a positive number")
    private Integer appointmentId;

    @NotBlank(message = "Symptoms are required")
    private String symptoms;

    @NotBlank(message = "Potential diagnosis is required")
    private String potentialDiagnosis;

    // Getters and Setters
    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
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
