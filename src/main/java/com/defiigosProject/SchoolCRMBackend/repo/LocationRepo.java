package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LocationRepo extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {
    Boolean existsByAddress(String address);
    Optional<Location> findByName(String name);
}
