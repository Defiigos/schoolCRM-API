package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "paymentStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList = new ArrayList<>();

    public PaymentStatus(PaymentStatusType status) {
        this.status = status;
    }

    public void addPayment(Payment payment){
        this.paymentList.add(payment);
        payment.setPaymentStatus(this);
    }

    public void removePayment(Payment payment){
        this.paymentList.remove(payment);
        payment.setPaymentStatus(null);
    }
}
