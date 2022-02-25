package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private User teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    private LessonStatus lessonStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private LessonDuration lessonDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonGroup> lessonGroupList = new ArrayList<>();

    public Lesson(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    public void addLessonGroup(LessonGroup lessonGroup){
        this.lessonGroupList.add(lessonGroup);
        lessonGroup.setLesson(this);
    }

    public void removeLessonGroup(LessonGroup lessonGroup){
        this.lessonGroupList.remove(lessonGroup);
        lessonGroup.setLesson(null);
    }
}
