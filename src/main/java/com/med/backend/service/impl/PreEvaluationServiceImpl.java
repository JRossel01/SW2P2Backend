package com.med.backend.service.impl;

import com.med.backend.dto.preevaluation.SavePreEvaluationDTO;
import com.med.backend.exception.DuplicateResourceException;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.Appointment;
import com.med.backend.persistence.entity.PreEvaluation;
import com.med.backend.persistence.repository.AppointmentRepository;
import com.med.backend.persistence.repository.PreEvaluationRepository;
import com.med.backend.service.PreEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreEvaluationServiceImpl implements PreEvaluationService {

    @Autowired
    private PreEvaluationRepository preEvaluationRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private static int lastUsedPreEvaluationId = 0; // Mantiene el último ID utilizado

    @Override
    public PreEvaluation registerOnePreEvaluation(SavePreEvaluationDTO newPreEvaluation) {
        // Verificar si ya existe una preevaluación asociada al appointment
        if (preEvaluationRepository.findByAppointmentId(newPreEvaluation.getAppointmentId()).isPresent()) {
            throw new DuplicateResourceException(
                    "PreEvaluation",
                    "appointmentId",
                    String.valueOf(newPreEvaluation.getAppointmentId()),
                    "A pre-evaluation already exists for the specified appointment."
            );
        }

        // Validar la existencia de la cita asociada
        Appointment appointment = appointmentRepository.findById(newPreEvaluation.getAppointmentId())
                .orElseThrow(() -> new
                        ObjectNotFoundException("Appointment not found for the given appointmentId: " + newPreEvaluation.getAppointmentId()));

        // Crear la nueva preevaluación
        PreEvaluation preEvaluation = new PreEvaluation();
        preEvaluation.setId(autoIncrement()); // Generar un ID único
        preEvaluation.setAppointmentId(newPreEvaluation.getAppointmentId());
        preEvaluation.setSymptoms(newPreEvaluation.getSymptoms());
        preEvaluation.setPotentialDiagnosis(newPreEvaluation.getPotentialDiagnosis());

        return preEvaluationRepository.save(preEvaluation);
    }

    @Override
    public PreEvaluation findPreEvaluationById(int preEvaluationId) {
        return preEvaluationRepository.findById(preEvaluationId)
                .orElseThrow(() -> new ObjectNotFoundException("Pre-evaluation not found for the given id: " + preEvaluationId));
    }

    @Override
    public List<PreEvaluation> getAllPreEvaluations() {
        return preEvaluationRepository.findAll();
    }

    @Override
    public PreEvaluation findPreEvaluationByAppointment(int appointmentId) {
        return preEvaluationRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ObjectNotFoundException("No pre-evaluation found for the given appointmentId: " + appointmentId));
    }

    // Método para auto-incrementar el ID de la preevaluación
    private int autoIncrement() {
        // Solo inicializa el último ID usado si aún no se ha establecido
        if (lastUsedPreEvaluationId == 0) {
            lastUsedPreEvaluationId = preEvaluationRepository.findAll().stream()
                    .map(PreEvaluation::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }

        // Incrementa y devuelve el nuevo ID
        lastUsedPreEvaluationId++;
        return lastUsedPreEvaluationId;
    }
}
