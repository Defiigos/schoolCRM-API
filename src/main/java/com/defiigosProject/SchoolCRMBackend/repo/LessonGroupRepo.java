package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.LessonGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonGroupRepo extends JpaRepository<LessonGroup, Long>, JpaSpecificationExecutor<LessonGroup> {
    Boolean existsByName(String name);
}
