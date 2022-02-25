package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
}
