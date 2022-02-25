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
    private LocalDate paymentDate;

    @Column(nullable = false)
    private LocalTime paymentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentAmount amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private PaymentStatus paymentStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private LessonGroup lessonGroup;
}
