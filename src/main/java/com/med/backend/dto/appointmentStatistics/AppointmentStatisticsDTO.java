package com.med.backend.dto.appointmentStatistics;

import com.fasterxml.jackson.annotation.JsonProperty;

// import java.time.LocalDateTime;

public class AppointmentStatisticsDTO {
    private String id;
    private String doctorId;
    private String doctorName;
    private String date;
    private int totalAppointments;
    private int completedAppointments;
    private int canceledAppointments;
    private double averageWaitTime;

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public int getCompletedAppointments() {
        return completedAppointments;
    }

    public void setCompletedAppointments(int completedAppointments) {
        this.completedAppointments = completedAppointments;
    }

    public int getCanceledAppointments() {
        return canceledAppointments;
    }

    public void setCanceledAppointments(int canceledAppointments) {
        this.canceledAppointments = canceledAppointments;
    }

    public double getAverageWaitTime() {
        return averageWaitTime;
    }

    public void setAverageWaitTime(double averageWaitTime) {
        this.averageWaitTime = averageWaitTime;
    }
}
