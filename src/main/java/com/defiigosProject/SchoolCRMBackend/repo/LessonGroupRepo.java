package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonGroupRepo extends JpaRepository<Lesson, Long> {
}