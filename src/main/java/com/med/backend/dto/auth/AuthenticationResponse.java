package com.med.backend.dto.auth;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private String jwt;
    private String role;
    private Integer doctorId; // Puede ser null si no aplica
    private Integer patientId; // Puede ser null si no aplica

    // Getters y Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}
