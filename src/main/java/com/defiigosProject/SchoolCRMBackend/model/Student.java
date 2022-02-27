package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;

    @Column(nullable = false)
    private String parentName;

    @Column(nullable = false)
    private String parentPhone;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudentStatus status;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> paymentList = new ArrayList<>();

    @ManyToMany(mappedBy = "students")
    private Set<LessonGroup> lessonGroups = new HashSet<>();

    public Student(String name, String phone,
                   String parentName, String parentPhone,
                   String description) {
        this.name = name;
        this.phone = phone;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.description = description;
    }

    public void addPayment(Payment payment){
        this.paymentList.add(payment);
        payment.setStudent(this);
    }

    public void removePayment(Payment payment){
        this.paymentList.remove(payment);
        payment.setStudent(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id)
                && Objects.equals(name, student.name)
                && Objects.equals(phone, student.phone)
                && parentName.equals(student.parentName)
                && parentPhone.equals(student.parentPhone)
                && Objects.equals(description, student.description)
                && Objects.equals(status, student.status)
                && Objects.equals(paymentList, student.paymentList)
                && Objects.equals(lessonGroups, student.lessonGroups);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
