package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.PaymentAmount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentAmountRepo extends JpaRepository<PaymentAmount, Long> {
}
