package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.StudentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.StudentStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentStatusRepo extends JpaRepository<StudentStatus, Long> {
    Optional<StudentStatus> findByStatus(StudentStatusType locationStatusType);
}
