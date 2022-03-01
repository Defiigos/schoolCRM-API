package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.LessonStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonStatusRepo extends JpaRepository<LessonStatus, Long> {
    Optional<LessonStatus> findByStatus(LessonStatusType lessonStatusType);
}
