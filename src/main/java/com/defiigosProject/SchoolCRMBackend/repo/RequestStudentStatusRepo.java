package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.RequestStudentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestStudentStatusRepo extends JpaRepository<RequestStudentStatus, Long> {
    Optional<RequestStudentStatus> findByStatus(RequestStudentStatusType requestStudentStatusType);
}
