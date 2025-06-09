package com.med.backend.controller;

import com.med.backend.dto.preevaluation.SavePreEvaluationDTO;
import com.med.backend.persistence.entity.PreEvaluation;
import com.med.backend.service.PreEvaluationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PreEvaluationController {

    @Autowired
    private PreEvaluationService preEvaluationService;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR','PATIENT')")
    @MutationMapping(name = "registerPreEvaluation")
    public PreEvaluation registerPreEvaluation(@Argument("preEvaluationInput") @Valid SavePreEvaluationDTO newPreEvaluation) {
        return preEvaluationService.registerOnePreEvaluation(newPreEvaluation);
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMINISTRATOR','PATIENT','RECEPTIONIST')")
    @QueryMapping(name = "findPreEvaluationById")
    public PreEvaluation findPreEvaluationById(@Argument("preEvaluationId") int preEvaluationId) {
        return preEvaluationService.findPreEvaluationById(preEvaluationId);
    }

    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMINISTRATOR','PATIENT','RECEPTIONIST')")
    @QueryMapping(name = "findPreEvaluationByAppointment")
    public PreEvaluation findPreEvaluationByAppointment(@Argument("appointmentId") int appointmentId) {
        return preEvaluationService.findPreEvaluationByAppointment(appointmentId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR')")
    @QueryMapping(name = "getAllPreEvaluations")
    public List<PreEvaluation> getAllPreEvaluations() {
        return preEvaluationService.getAllPreEvaluations();
    }
}
