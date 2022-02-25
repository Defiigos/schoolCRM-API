package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lesson_groups")
@Getter
@Setter
@NoArgsConstructor
public class LessonGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @OneToOne(
            mappedBy = "lessonGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Payment payment;

    public void setPayment(Payment payment){
        if (payment == null){
            if (this.payment != null)
                this.payment.setLessonGroup(null);
        } else {
            payment.setLessonGroup(this);
        }
        this.payment = payment;
    }
}
