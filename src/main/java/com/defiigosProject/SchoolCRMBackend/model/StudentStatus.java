package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.StudentStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student_statuses")
@Getter
@Setter
@NoArgsConstructor
public class StudentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private StudentStatusType status;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students = new HashSet<>();

    public StudentStatus(StudentStatusType status) {
        this.status = status;
    }

    public void addStudent(Student student){
        this.students.add(student);
        student.setStatus(this);
    }

    public void removeStudent(Student student){
        this.students.remove(student);
        student.setStatus(null);
    }
}
