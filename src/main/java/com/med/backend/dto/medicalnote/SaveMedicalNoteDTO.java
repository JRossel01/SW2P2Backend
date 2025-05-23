package com.med.backend.dto.medicalnote;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class SaveMedicalNoteDTO {

    @NotBlank(message = "Note type is required")
    private String noteType;

    @NotBlank(message = "Details are required")
    private String details;

    @NotNull(message = "Date is required")
    private String date;

    @NotNull(message = "Medical Record ID is required")
    private Integer medicalRecordId;

    // Getters and Setters
    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(Integer medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }
}
