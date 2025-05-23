package com.med.backend.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "medical_notes") // Define la colección como "medical_notes" en MongoDB
public class MedicalNote {

    @Id
    private int id;

    private String noteType;
    private String details;
    private String date;

    // Llave foránea para la relación con MedicalRecord
    private int medicalRecordId;

    public MedicalNote() {
    }

    public MedicalNote(int id, String noteType, String details, String date, int medicalRecordId) {
        this.id = id;
        this.noteType = noteType;
        this.details = details;
        this.date = date;
        this.medicalRecordId = medicalRecordId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }
}
