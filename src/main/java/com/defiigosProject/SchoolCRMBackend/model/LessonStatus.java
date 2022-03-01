package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lesson_statuses")
@Getter
@Setter
@NoArgsConstructor
public class LessonStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private LessonStatusType status;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();

    public LessonStatus(LessonStatusType status) {
        this.status = status;
    }

    public void addLesson(Lesson lesson){
        this.lessons.add(lesson);
        lesson.setStatus(this);
    }

    public void removeLesson(Lesson lesson){
        this.lessons.remove(lesson);
        lesson.setStatus(null);
    }
}
