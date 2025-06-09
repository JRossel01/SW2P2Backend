package com.med.backend.controller;

import com.med.backend.dto.appointment.SaveAppointmentDTO;
import com.med.backend.dto.appointment.UpdateAppointmentDTO;
import com.med.backend.dto.appointmentStatistics.AppointmentStatisticsDTO;
import com.med.backend.persistence.entity.Appointment;
import com.med.backend.service.AppointmentService;
import com.med.backend.service.DoctorService;
import com.med.backend.service.Webhook.AppointmentStatisticsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentStatisticsService appointmentStatisticsService;

     @Autowired
    private DoctorService doctorService;


    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST','PATIENT')")
    @MutationMapping(name = "registerAppointment")
    public Appointment registerAppointment(@Argument("appointmentInput") @Valid SaveAppointmentDTO newAppointment) {
        //  return appointmentService.registerOneAppointment(newAppointment);

        Appointment appointment = appointmentService.registerOneAppointment(newAppointment);

        // Preparar estadísticas diarias iniciales al registrar una cita
        AppointmentStatisticsDTO statistics = new AppointmentStatisticsDTO();
        statistics.setId(String.valueOf(appointment.getId()));
        statistics.setDoctorId(String.valueOf(appointment.getDoctorId()));
        statistics.setDoctorName(doctorService.getDoctorWithUserById(appointment.getDoctorId()).getName());
        statistics.setDate(appointment.getDate().toString());
        statistics.setTotalAppointments(1);

        // Enviar estadísticas sin completar ni cancelar aún
        appointmentStatisticsService.sendDailyAppointmentStatistics(statistics);

        return appointment;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST')")
    @MutationMapping(name = "updateAppointment")
    public Appointment updateAppointment(@Argument("appointmentId") int appointmentId,
                                         @Argument("appointmentInput") @Valid UpdateAppointmentDTO updatedAppointment) {
        return appointmentService.updateAppointment(appointmentId, updatedAppointment);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST')")
    @MutationMapping(name = "deleteAppointment")
    public void deleteAppointment(@Argument("appointmentId") int appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST','PATIENT')")
    @QueryMapping(name = "getAllAppointments")
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST','PATIENT')")
    @QueryMapping(name = "getAppointmentsByPatient")
    public List<Appointment> getAppointmentsByPatient(@Argument("patientId") int patientId) {
        return appointmentService.getAllByPatient(patientId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST', 'DOCTOR')")
    @QueryMapping(name = "getAppointmentsByDoctor")
    public List<Appointment> getAppointmentsByDoctor(@Argument("doctorId") int doctorId) {
        return appointmentService.getAllByDoctor(doctorId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST', 'DOCTOR')")
    @QueryMapping(name = "getAppointmentById")
    public Appointment getAppointmentById(@Argument("appointmentId") int appointmentId) {
        return appointmentService.getAppointmentById(appointmentId);
    }

    @MutationMapping(name = "updateAppointmentStatus")
    public Appointment updateAppointmentStatus(@Argument("appointmentId") int appointmentId,
                                              @Argument("status") String status) {
        return appointmentService.updateAppointmentStatus(appointmentId, status);
    }
}
