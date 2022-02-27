package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.LocationStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationStatusRepo extends JpaRepository<LocationStatus, Long> {
    Optional<LocationStatus> findByStatus(LocationStatusType locationStatusType);
}
