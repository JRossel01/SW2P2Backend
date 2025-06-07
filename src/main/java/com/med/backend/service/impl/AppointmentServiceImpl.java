package com.med.backend.service.impl;

import com.med.backend.dto.appointment.SaveAppointmentDTO;
import com.med.backend.dto.appointment.UpdateAppointmentDTO;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.Appointment;
import com.med.backend.persistence.repository.AppointmentRepository;
import com.med.backend.persistence.repository.DoctorRepository;
import com.med.backend.persistence.repository.PatientRepository;
import com.med.backend.persistence.repository.UserRepository;
import com.med.backend.service.AppointmentService;
import com.med.backend.service.twilio.WhatsAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    private static int lastUsedAppointmentId = 0;

    @Override
    public Appointment registerOneAppointment(SaveAppointmentDTO newAppointment) {
        validateDoctorAndPatientExistence(newAppointment.getDoctorId(), newAppointment.getPatientId());

        // Normalizar el formato del tiempo a "HH:mm"
        LocalTime normalizedTime = LocalTime.parse(newAppointment.getTime(), DateTimeFormatter.ofPattern("H:mm"));
        String formattedTime = normalizedTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        Appointment appointment = new Appointment();
        appointment.setId(autoIncrement());
        appointment.setDate(newAppointment.getDate());
        appointment.setTime(formattedTime);
        appointment.setStatus("pending");
        appointment.setReason(newAppointment.getReason());
        appointment.setPatientId(newAppointment.getPatientId());
        appointment.setDoctorId(newAppointment.getDoctorId());


        Appointment savedAppointment = appointmentRepository.save(appointment);
        sendWhatsAppConfirmation(savedAppointment);

        return savedAppointment;
    }

    private void sendWhatsAppConfirmation(Appointment appointment) {
        var doctor = doctorRepository.findById(appointment.getDoctorId()).orElse(null);
        var patient = patientRepository.findById(appointment.getPatientId()).orElse(null);

        if (doctor != null && patient != null) {
            var doctorUser = userRepository.findById(doctor.getUserId()).orElse(null);
            String patientPhone = patient.getPhone();

            if (doctorUser != null && patientPhone != null) {
                String doctorName = doctorUser.getName();

                whatsAppService.sendAppointmentConfirmation(
                        patientPhone,
                        doctorName,
                        appointment.getDate(),
                        appointment.getTime(),
                        appointment.getReason()
                );
            }
        }
    }





    @Override
    public Appointment updateAppointment(int appointmentId, UpdateAppointmentDTO updatedAppointmentData) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found"));

        if (updatedAppointmentData.getDate() != null) {
            appointment.setDate(updatedAppointmentData.getDate());
        }
        if (updatedAppointmentData.getTime() != null) {
            // Normalizar el formato del tiempo a "HH:mm"
            LocalTime normalizedTime = LocalTime.parse(updatedAppointmentData.getTime(), DateTimeFormatter.ofPattern("H:mm"));
            String formattedTime = normalizedTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            appointment.setTime(formattedTime);
        }
        if (updatedAppointmentData.getStatus() != null) {
            appointment.setStatus(updatedAppointmentData.getStatus());
        }
        if (updatedAppointmentData.getReason() != null) {
            appointment.setReason(updatedAppointmentData.getReason());
        }
        if (updatedAppointmentData.getPatientId() != null && !updatedAppointmentData.getPatientId().equals(appointment.getPatientId())) {
            validatePatientExistence(updatedAppointmentData.getPatientId());
            appointment.setPatientId(updatedAppointmentData.getPatientId());
        }
        if (updatedAppointmentData.getDoctorId() != null && !updatedAppointmentData.getDoctorId().equals(appointment.getDoctorId())) {
            validateDoctorExistence(updatedAppointmentData.getDoctorId());
            appointment.setDoctorId(updatedAppointmentData.getDoctorId());
        }

        return appointmentRepository.save(appointment);
    }


    @Override
    public void deleteAppointment(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found"));
        appointmentRepository.deleteById(appointmentId);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<Appointment> getAllByPatient(int patientId) {
        validatePatientExistence(patientId);
        return appointmentRepository.findByPatientId(patientId);
    }

    @Override
    public List<Appointment> getAllByDoctor(int doctorId) {
        validateDoctorExistence(doctorId);
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Override
    public Appointment getAppointmentById(int appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found"));
    }

    @Override
    public Appointment updateAppointmentStatus(int appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found"));
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    private int autoIncrement() {
        if (lastUsedAppointmentId == 0) {
            lastUsedAppointmentId = appointmentRepository.findAll().stream()
                    .map(Appointment::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }
        lastUsedAppointmentId++;
        return lastUsedAppointmentId;
    }

    private void validateDoctorAndPatientExistence(int doctorId, int patientId) {
        validateDoctorExistence(doctorId);
        validatePatientExistence(patientId);
    }

    private void validateDoctorExistence(int doctorId) {
        if (doctorRepository.findById(doctorId).isEmpty()) {
            throw new ObjectNotFoundException("Doctor not found for the given doctorId: " + doctorId);
        }
    }

    private void validatePatientExistence(int patientId) {
        if (patientRepository.findById(patientId).isEmpty()) {
            throw new ObjectNotFoundException("Patient not found for the given patientId: " + patientId);
        }
    }
}
