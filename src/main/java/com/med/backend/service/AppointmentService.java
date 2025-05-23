package com.med.backend.service;

import com.med.backend.dto.appointment.SaveAppointmentDTO;
import com.med.backend.dto.appointment.UpdateAppointmentDTO;
import com.med.backend.persistence.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment registerOneAppointment(SaveAppointmentDTO newAppointment);

    Appointment updateAppointment(int appointmentId, UpdateAppointmentDTO updatedAppointmentData);

    void deleteAppointment(int appointmentId);

    List<Appointment> getAllAppointments();

    List<Appointment> getAllByPatient(int patientId);

    List<Appointment> getAllByDoctor(int doctorId);

    Appointment getAppointmentById(int appointmentId);

    Appointment updateAppointmentStatus(int appointmentId, String status);
}
