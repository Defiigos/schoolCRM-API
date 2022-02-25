package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.LessonStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonStatusRepo extends JpaRepository<LessonStatus, Long> {
}
