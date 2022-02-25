package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private StudentStatus studentStatus;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonGroup> lessonGroupList = new ArrayList<>();

    public Student(String name, String phone,
                   String parentName, String parentPhone,
                   String description) {
        this.name = name;
        this.phone = phone;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.description = description;
    }

    public void addLessonGroup(LessonGroup lessonGroup){
        this.lessonGroupList.add(lessonGroup);
        lessonGroup.setStudent(this);
    }

    public void removeLessonGroup(LessonGroup lessonGroup){
        this.lessonGroupList.remove(lessonGroup);
        lessonGroup.setStudent(null);
    }
}
