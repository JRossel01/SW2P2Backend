package com.med.backend.persistence.repository;

import com.med.backend.persistence.entity.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends MongoRepository<Schedule,Integer> {

    List<Schedule> findByDayOfWeekAndDoctorId(String dayOfWeek, Integer doctorId);
    List<Schedule> findByDoctorId(int doctorId);

}
