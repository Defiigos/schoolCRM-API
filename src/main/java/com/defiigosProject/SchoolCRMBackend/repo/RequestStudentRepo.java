package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.RequestStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequestStudentRepo extends JpaRepository<RequestStudent, Long>, JpaSpecificationExecutor<RequestStudent> {
}
