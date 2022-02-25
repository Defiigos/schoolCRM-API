package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.StudentStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "studentStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> studentList = new ArrayList<>();

    public StudentStatus(StudentStatusType status) {
        this.status = status;
    }

    public void addStudent(Student student){
        this.studentList.add(student);
        student.setStudentStatus(this);
    }

    public void removeStudent(Student student){
        this.studentList.remove(student);
        student.setStudentStatus(null);
    }
}
