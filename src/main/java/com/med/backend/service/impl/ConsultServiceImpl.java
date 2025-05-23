package com.med.backend.service.impl;

import com.med.backend.dto.consult.SaveConsultDTO;
import com.med.backend.dto.consult.UpdateConsultDTO;
import com.med.backend.exception.DuplicateResourceException;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.Consult;
import com.med.backend.persistence.entity.MedicalRecord;
import com.med.backend.persistence.entity.Appointment;
import com.med.backend.persistence.repository.AppointmentRepository;
import com.med.backend.persistence.repository.ConsultRepository;
import com.med.backend.persistence.repository.MedicalRecordRepository;
import com.med.backend.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultServiceImpl implements ConsultService {

    @Autowired
    private ConsultRepository consultRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private static int lastUsedConsultId = 0;

    @Override
    public Consult findConsultByAppointment(int appointmentId) {
        return consultRepository.findConsultByAppointmentId(appointmentId)
                .orElseThrow(() -> new ObjectNotFoundException("No consult found for the given appointmentId: " + appointmentId));
    }

    @Override
    public Consult registerOneConsult(SaveConsultDTO newConsult) {
        Appointment appointment = appointmentRepository.findById(newConsult.getAppointmentId())
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found for the given appointmentId: " + newConsult.getAppointmentId()));

        // Verificar si ya existe un consult asociado al appointment
        if (consultRepository.findByAppointmentId(newConsult.getAppointmentId()).isPresent()) {
            throw new DuplicateResourceException(
                    "Consult",
                    "appointmentId",
                    String.valueOf(newConsult.getAppointmentId()),
                    "A consult already exists for the specified appointment."
            );
        }

        // Obtener el medicalRecordId a partir del patientId del appointment
        int patientId = appointment.getPatientId();
        MedicalRecord medicalRecord = medicalRecordRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "No se encontró una historia clínica para el " +
                                "paciente con ID: " + patientId + ". " +
                                "Por favor, cree una historia clínica para este paciente antes de proceder.")
                );

        // Usar la fecha actual
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Consult consult = new Consult();
        consult.setId(autoIncrement());
        consult.setDate(currentDate);
        consult.setDiagnosis(newConsult.getDiagnosis());
        consult.setTreatment(newConsult.getTreatment());
        consult.setObservations(newConsult.getObservations() != null ? newConsult.getObservations() : "");
        consult.setCurrentWeight(newConsult.getCurrentWeight() != null ? newConsult.getCurrentWeight() : 0.0);
        consult.setCurrentHeight(newConsult.getCurrentHeight() != null ? newConsult.getCurrentHeight() : 0.0);
        consult.setMedicalRecordId(medicalRecord.getId());
        consult.setAppointmentId(newConsult.getAppointmentId());

        // Nuevo campo de hora de atención (si es proporcionado)
        consult.setAttentionTime(newConsult.getAttentionTime() != null ? newConsult.getAttentionTime() : "");

        // Guardar la consulta
        Consult savedConsult = consultRepository.save(consult);

        // Actualizar el estado del appointment a "COMPLETE"
        appointment.setStatus("complete");
        appointmentRepository.save(appointment);

        return savedConsult;
    }


    @Override
    public List<Consult> getAllConsults() {
        return consultRepository.findAll();
    }

    @Override
    public Consult updateConsult(int consultId, UpdateConsultDTO updatedConsultData) {
        Consult consult = consultRepository.findById(consultId)
                .orElseThrow(() -> new ObjectNotFoundException("Consult not found"));

        if (updatedConsultData.getDate() != null) {
            consult.setDate(updatedConsultData.getDate());
        }
        if (updatedConsultData.getDiagnosis() != null) {
            consult.setDiagnosis(updatedConsultData.getDiagnosis());
        }
        if (updatedConsultData.getTreatment() != null) {
            consult.setTreatment(updatedConsultData.getTreatment());
        }
        if (updatedConsultData.getObservations() != null) {
            consult.setObservations(updatedConsultData.getObservations());
        }
        if (updatedConsultData.getCurrentWeight() != null) {
            consult.setCurrentWeight(updatedConsultData.getCurrentWeight());
        }
        if (updatedConsultData.getCurrentHeight() != null) {
            consult.setCurrentHeight(updatedConsultData.getCurrentHeight());
        }

        // Actualizar el nuevo campo attentionTime
        if (updatedConsultData.getAttentionTime() != null) {
            consult.setAttentionTime(updatedConsultData.getAttentionTime());
        }

        // No permitimos cambiar el medicalRecordId o appointmentId
        return consultRepository.save(consult);
    }

    @Override
    public void deleteConsult(int consultId) {
        Consult consult = consultRepository.findById(consultId)
                .orElseThrow(() -> new ObjectNotFoundException("Consult not found"));
        consultRepository.deleteById(consultId);
    }

    @Override
    public List<Consult> findConsultsByMedicalRecordId(int medicalRecordId) {
        validateMedicalRecordExistence(medicalRecordId);
        return consultRepository.findByMedicalRecordId(medicalRecordId);
    }

    @Override
    public List<Consult> findConsultsByDoctor(int doctorId) {
        // Encontrar todas las citas del doctor
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        if (appointments.isEmpty()) {
            throw new ObjectNotFoundException("No appointments found for the given doctorId: " + doctorId);
        }
        // Obtener los IDs de las citas y buscar las consultas asociadas
        List<Integer> appointmentIds = appointments.stream().map(Appointment::getId).collect(Collectors.toList());
        return consultRepository.findByAppointmentIdIn(appointmentIds);
    }

    @Override
    public List<Consult> findConsultsByPatient(int patientId) {
        // Encontrar todas las citas del paciente
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        if (appointments.isEmpty()) {
            throw new ObjectNotFoundException("No appointments found for the given patientId: " + patientId);
        }
        // Obtener los IDs de las citas y buscar las consultas asociadas
        List<Integer> appointmentIds = appointments.stream().map(Appointment::getId).collect(Collectors.toList());
        return consultRepository.findByAppointmentIdIn(appointmentIds);
    }

    private int autoIncrement() {
        if (lastUsedConsultId == 0) {
            lastUsedConsultId = consultRepository.findAll().stream()
                    .map(Consult::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }
        lastUsedConsultId++;
        return lastUsedConsultId;
    }

    private MedicalRecord validateMedicalRecordExistence(int medicalRecordId) {
        return medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ObjectNotFoundException("Medical record not found for the given medicalRecordId: " + medicalRecordId));
    }

    private Appointment validateAppointmentExistence(int appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Appointment not found for the given appointmentId: " + appointmentId));
    }
}
