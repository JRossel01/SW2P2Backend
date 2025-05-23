package com.med.backend.service;

import com.med.backend.dto.preevaluation.SavePreEvaluationDTO;
import com.med.backend.persistence.entity.PreEvaluation;

import java.util.List;

public interface PreEvaluationService {
    PreEvaluation registerOnePreEvaluation(SavePreEvaluationDTO newPreEvaluation);
    PreEvaluation findPreEvaluationById(int preEvaluationId);
    PreEvaluation findPreEvaluationByAppointment(int appointmentId);
    List<PreEvaluation> getAllPreEvaluations();
}
