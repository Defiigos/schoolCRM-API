package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.PaymentStatus;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepo extends JpaRepository<PaymentStatus, Long> {
    Optional<PaymentStatus> findByStatus(PaymentStatusType paymentStatusType);
}
