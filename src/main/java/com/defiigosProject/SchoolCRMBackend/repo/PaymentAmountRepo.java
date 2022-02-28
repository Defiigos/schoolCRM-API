package com.defiigosProject.SchoolCRMBackend.repo;

import com.defiigosProject.SchoolCRMBackend.model.PaymentAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentAmountRepo extends JpaRepository<PaymentAmount, Long>, JpaSpecificationExecutor<PaymentAmount> {
    Boolean existsBySum(Float sum);
}
