package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_amounts")
@Getter
@Setter
@NoArgsConstructor
public class PaymentAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int sum;
    private String name;

    @OneToMany(mappedBy = "amount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList = new ArrayList<>();

    public PaymentAmount(int sum, String name) {
        this.sum = sum;
        this.name = name;
    }

    public void addPayment(Payment payment){
        this.paymentList.add(payment);
        payment.setAmount(this);
    }

    public void removePayment(Payment payment){
        this.paymentList.remove(payment);
        payment.setAmount(null);
    }
}
