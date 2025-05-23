package com.med.backend.controller;

import com.med.backend.dto.appointmentStatistics.AppointmentStatisticsDTO;
import com.med.backend.dto.consult.SaveConsultDTO;
import com.med.backend.dto.consult.UpdateConsultDTO;
import com.med.backend.persistence.entity.Appointment;
import com.med.backend.persistence.entity.Consult;
import com.med.backend.service.AppointmentService;
import com.med.backend.service.ConsultService;
import com.med.backend.service.DoctorService;
import com.med.backend.service.Webhook.AppointmentStatisticsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.Duration;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class ConsultController {

    @Autowired
    private ConsultService consultService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentStatisticsService appointmentStatisticsService;

    @Autowired
    private DoctorService doctorService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','DOCTOR')")
    @MutationMapping(name = "registerConsult")
    public Consult registerConsult(@Argument("consultInput") @Valid SaveConsultDTO newConsult) {
        //   return consultService.registerOneConsult(newConsult);
         Consult consult = consultService.registerOneConsult(newConsult);

        // Obtener la cita asociada
        Appointment appointment = appointmentService.getAppointmentById(consult.getAppointmentId());

        // Calcular tiempo de espera
        LocalTime scheduledTime = LocalTime.parse(appointment.getTime()); // Asegúrate de que está en formato HH:mm
        LocalTime consultTime = LocalTime.parse(consult.getAttentionTime()); // Asegúrate de que está en formato HH:mm
        long waitTimeMinutes = Duration.between(scheduledTime,consultTime).toMinutes();

        // Preparar estadísticas de cita completada
        AppointmentStatisticsDTO statistics = new AppointmentStatisticsDTO();
        statistics.setDoctorId(String.valueOf(appointment.getDoctorId()));
        statistics.setDoctorName(doctorService.getDoctorWithUserById(appointment.getDoctorId()).getName());
        statistics.setDate(appointment.getDate().toString());
        statistics.setCompletedAppointments(1);
        statistics.setAverageWaitTime(waitTimeMinutes);

        appointmentStatisticsService.sendDailyAppointmentStatistics(statistics);

        return consult;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @QueryMapping(name = "getAllConsults")
    public List<Consult> getAllConsults() {
        return consultService.getAllConsults();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'DOCTOR')")
    @MutationMapping(name = "updateConsult")
    public Consult updateConsult(@Argument("consultId") int consultId,
                                 @Argument("consultInput") @Valid UpdateConsultDTO updatedConsult) {
        return consultService.updateConsult(consultId, updatedConsult);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "deleteConsult")
    public void deleteConsult(@Argument("consultId") int consultId) {
        consultService.deleteConsult(consultId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'DOCTOR')")
    @QueryMapping(name = "findConsultsByDoctor")
    public List<Consult> findConsultsByDoctor(@Argument("doctorId") int doctorId) {
        return consultService.findConsultsByDoctor(doctorId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'DOCTOR','RECEPTIONIST')")
    @QueryMapping(name = "findConsultsByAppointment")
    public Consult findConsultsByAppointment(@Argument("appointmentId") int appointmentId) {
        return consultService.findConsultByAppointment(appointmentId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'DOCTOR', 'PATIENT')")
    @QueryMapping(name = "findConsultsByPatient")
    public List<Consult> findConsultsByPatient(@Argument("patientId") int patientId) {
        return consultService.findConsultsByPatient(patientId);
    }
}
