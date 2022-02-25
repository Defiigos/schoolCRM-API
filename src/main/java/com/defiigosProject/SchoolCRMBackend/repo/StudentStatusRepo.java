package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.StudentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentStatusRepo extends JpaRepository<StudentStatus, Long> {
}
