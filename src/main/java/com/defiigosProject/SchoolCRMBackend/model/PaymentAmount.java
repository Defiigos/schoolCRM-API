package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_amounts")
@Getter
@Setter
@NoArgsConstructor
public class PaymentAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Float sum;
    private String name;

    @OneToMany(mappedBy = "amount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    public PaymentAmount(Float sum, String name) {
        this.sum = sum;
        this.name = name;
    }

    public void addPayment(Payment payment){
        this.payments.add(payment);
        payment.setAmount(this);
    }

    public void removePayment(Payment payment){
        this.payments.remove(payment);
        payment.setAmount(null);
    }
}
