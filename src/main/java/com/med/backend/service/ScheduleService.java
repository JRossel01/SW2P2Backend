package com.med.backend.service;

import com.med.backend.dto.schedule.SaveScheduleDTO;
import com.med.backend.dto.schedule.UpdateScheduleDTO;
import com.med.backend.persistence.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule registerOneSchedule(SaveScheduleDTO newSchedule);
    List<Schedule> getAllSchedules();
    Schedule updateSchedule(int scheduleId, UpdateScheduleDTO updatedScheduleData);
    void deleteSchedule(int scheduleId);
    List<Schedule> getSchedulesByDoctor(int doctorId);
    Schedule getScheduleById(int scheduleId);
}
