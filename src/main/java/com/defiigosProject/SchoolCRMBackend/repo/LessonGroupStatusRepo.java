package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.LessonGroupStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonGroupStatusRepo extends JpaRepository<LessonGroupStatus, Long> {
    Optional<LessonGroupStatus> findByStatus(LessonGroupStatusType locationStatusType);
}
