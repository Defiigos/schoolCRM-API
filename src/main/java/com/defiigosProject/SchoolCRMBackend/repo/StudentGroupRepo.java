package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.LessonGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentGroupRepo extends JpaRepository<LessonGroup, Long> {
}
