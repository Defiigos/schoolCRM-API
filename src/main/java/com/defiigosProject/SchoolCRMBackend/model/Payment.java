package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate payDate;

    @Column(nullable = false)
    private LocalTime payTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentAmount amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    public Payment(LocalDate payDate, LocalTime payTime) {
        this.payDate = payDate;
        this.payTime = payTime;
    }
}
