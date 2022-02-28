package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.LessonDuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalTime;

public interface LessonDurationRepo extends JpaRepository<LessonDuration, Long>, JpaSpecificationExecutor<LessonDuration> {
    Boolean existsByTime(LocalTime time);
}
