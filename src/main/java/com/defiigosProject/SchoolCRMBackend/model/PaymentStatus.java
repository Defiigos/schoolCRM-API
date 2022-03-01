package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_statuses")
@Getter
@Setter
@NoArgsConstructor
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatusType status;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    public PaymentStatus(PaymentStatusType status) {
        this.status = status;
    }

    public void addPayment(Payment payment){
        this.payments.add(payment);
        payment.setStatus(this);
    }

    public void removePayment(Payment payment){
        this.payments.remove(payment);
        payment.setStatus(null);
    }
}
