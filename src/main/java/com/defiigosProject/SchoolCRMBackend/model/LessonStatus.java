package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private LessonStatusType type;

    @OneToMany(mappedBy = "lessonStatus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessonList = new ArrayList<>();

    public LessonStatus(LessonStatusType type) {
        this.type = type;
    }

    public void addLesson(Lesson lesson){
        this.lessonList.add(lesson);
        lesson.setLessonStatus(this);
    }

    public void removeLesson(Lesson lesson){
        this.lessonList.remove(lesson);
        lesson.setLessonStatus(null);
    }
}
