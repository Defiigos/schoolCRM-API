package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    private LessonStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    private LessonDuration duration;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    private LessonGroup lessonGroup;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Payment> payments = new HashSet<>();

    public Lesson(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    public void addPayment(Payment payment){
        this.payments.add(payment);
        payment.setLesson(this);
    }

    public void removePayment(Payment payment){
        this.payments.remove(payment);
        payment.setLesson(null);
    }
}
