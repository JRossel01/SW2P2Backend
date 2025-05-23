package com.med.backend.service.impl;

import com.med.backend.dto.schedule.SaveScheduleDTO;
import com.med.backend.dto.schedule.UpdateScheduleDTO;
import com.med.backend.exception.DuplicateResourceException;
import com.med.backend.exception.ObjectNotFoundException;
import com.med.backend.persistence.entity.Schedule;
import com.med.backend.persistence.repository.DoctorRepository;
import com.med.backend.persistence.repository.ScheduleRepository;
import com.med.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");


    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    private static int lastUsedScheduleId = 0;

    @Override
    public Schedule registerOneSchedule(SaveScheduleDTO newSchedule) {
        // Normalizar el formato del tiempo a "HH:mm"
        LocalTime normalizedStartTime = LocalTime.parse(newSchedule.getStartTime(), TIME_FORMATTER);
        LocalTime normalizedEndTime = LocalTime.parse(newSchedule.getEndTime(), TIME_FORMATTER);

        String formattedStartTime = normalizedStartTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String formattedEndTime = normalizedEndTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        validateTimeOverlap(newSchedule.getDayOfWeek(), newSchedule.getDoctorId(), formattedStartTime, formattedEndTime, null);

        if (formattedStartTime.compareTo(formattedEndTime) >= 0) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin.");
        }

        if (newSchedule.getStartTime() == null || newSchedule.getEndTime() == null) {
            throw new IllegalArgumentException("La hora de inicio y la hora de fin son obligatorias.");
        }
        if (newSchedule.getDoctorId() == null || newSchedule.getDoctorId() <= 0) {
            throw new IllegalArgumentException("Debe proporcionarse un doctorId válido.");
        }

        Schedule schedule = new Schedule();
        schedule.setId(autoIncrement());
        schedule.setDayOfWeek(newSchedule.getDayOfWeek());
        schedule.setStartTime(formattedStartTime);
        schedule.setEndTime(formattedEndTime);
        schedule.setDoctorId(newSchedule.getDoctorId());

        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getSchedulesByDoctor(int doctorId) {
        if (doctorId <= 0) {
            throw new IllegalArgumentException("Invalid doctorId provided.");
        }

        // Verificar si el doctor existe en tu base de datos (ajusta según tu implementación)
        boolean doctorExists = doctorRepository.findById(doctorId).isPresent();
        if (!doctorExists) {
            throw new ObjectNotFoundException("Doctor not found for the given doctorId: " + doctorId);
        }

        return scheduleRepository.findByDoctorId(doctorId);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule updateSchedule(int scheduleId, UpdateScheduleDTO updatedScheduleData) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ObjectNotFoundException("Schedule not found"));

        // Normalizar el tiempo si se proporciona un valor
        String startTime = updatedScheduleData.getStartTime() != null
                ? LocalTime.parse(updatedScheduleData.getStartTime(), TIME_FORMATTER).format(DateTimeFormatter.ofPattern("HH:mm"))
                : schedule.getStartTime();

        String endTime = updatedScheduleData.getEndTime() != null
                ? LocalTime.parse(updatedScheduleData.getEndTime(), TIME_FORMATTER).format(DateTimeFormatter.ofPattern("HH:mm"))
                : schedule.getEndTime();

        // Validar la superposición de horarios si se proporcionan cambios en el día o el doctor
        if (updatedScheduleData.getDayOfWeek() != null && updatedScheduleData.getDoctorId() != null) {
            validateTimeOverlap(updatedScheduleData.getDayOfWeek(),
                    updatedScheduleData.getDoctorId(), startTime, endTime, scheduleId);
        }

        // Actualiza la información del Schedule solo si se proporciona un valor
        if (updatedScheduleData.getDayOfWeek() != null) {
            schedule.setDayOfWeek(updatedScheduleData.getDayOfWeek());
        }
        if (updatedScheduleData.getStartTime() != null) {
            schedule.setStartTime(startTime);
        }
        if (updatedScheduleData.getEndTime() != null) {
            schedule.setEndTime(endTime);
        }
        if (updatedScheduleData.getDoctorId() != null && !updatedScheduleData.getDoctorId().equals(schedule.getDoctorId())) {
            schedule.setDoctorId(updatedScheduleData.getDoctorId());
        }

        if (schedule.getStartTime().compareTo(schedule.getEndTime()) >= 0) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule getScheduleById(int scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ObjectNotFoundException("Schedule not found"));
    }

    @Override
    public void deleteSchedule(int scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ObjectNotFoundException("Schedule not found"));
        scheduleRepository.deleteById(scheduleId);
    }

    private int autoIncrement() {
        if (lastUsedScheduleId == 0) {
            lastUsedScheduleId = scheduleRepository.findAll().stream()
                    .map(Schedule::getId)
                    .max(Integer::compareTo)
                    .orElse(0);
        }
        lastUsedScheduleId++;
        return lastUsedScheduleId;
    }


    private void validateTimeOverlap(String dayOfWeek, Integer doctorId, String startTime, String endTime, Integer scheduleIdToExclude) {
        LocalTime newStartTime = LocalTime.parse(startTime, TIME_FORMATTER);
        LocalTime newEndTime = LocalTime.parse(endTime, TIME_FORMATTER);

        List<Schedule> existingSchedules = scheduleRepository.findByDayOfWeekAndDoctorId(dayOfWeek, doctorId);

        for (Schedule existingSchedule : existingSchedules) {
            // Si es el mismo horario que estamos actualizando, omitirlo
            if (scheduleIdToExclude != null && existingSchedule.getId() == scheduleIdToExclude) {
                continue;
            }

            // Parsear los tiempos existentes
            LocalTime existingStartTime = LocalTime.parse(existingSchedule.getStartTime(), TIME_FORMATTER);
            LocalTime existingEndTime = LocalTime.parse(existingSchedule.getEndTime(), TIME_FORMATTER);

            // Verificar si hay superposición
            if (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime)) {
                throw new DuplicateResourceException(
                        "Schedule", "time range", startTime + " - " + endTime,
                        "A conflicting schedule already exists for this doctor on this day. Please choose a different time range."
                );
            }
        }
    }

}
