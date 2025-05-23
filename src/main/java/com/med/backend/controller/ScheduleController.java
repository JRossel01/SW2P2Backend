package com.med.backend.controller;

import com.med.backend.dto.schedule.SaveScheduleDTO;
import com.med.backend.dto.schedule.UpdateScheduleDTO;
import com.med.backend.persistence.entity.Schedule;
import com.med.backend.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "registerSchedule")
    public Schedule registerSchedule(@Argument("scheduleInput") @Valid SaveScheduleDTO newSchedule) {
        return scheduleService.registerOneSchedule(newSchedule);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @QueryMapping(name = "getAllSchedules")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST', 'DOCTOR')")
    @QueryMapping(name = "getSchedulesByDoctor")
    public List<Schedule> getSchedulesByDoctor(@Argument("doctorId") int doctorId) {
        return scheduleService.getSchedulesByDoctor(doctorId);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "updateSchedule")
    public Schedule updateSchedule(@Argument("scheduleId") int scheduleId,
                                   @Argument("scheduleInput") @Valid UpdateScheduleDTO updatedSchedule) {
        return scheduleService.updateSchedule(scheduleId, updatedSchedule);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @MutationMapping(name = "deleteSchedule")
    public void deleteSchedule(@Argument("scheduleId") int scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'RECEPTIONIST', 'DOCTOR')")
    @QueryMapping(name = "getScheduleById")
    public Schedule getScheduleById(@Argument("scheduleId") int scheduleId) {
        return scheduleService.getScheduleById(scheduleId);
    }
}
