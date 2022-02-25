package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location, Long> {
}
