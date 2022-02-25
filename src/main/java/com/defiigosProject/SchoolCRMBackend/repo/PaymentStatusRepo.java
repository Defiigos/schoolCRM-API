package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepo extends JpaRepository<PaymentStatus, Long> {
}
