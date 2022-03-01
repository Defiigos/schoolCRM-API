package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lesson_durations")
@Getter
@Setter
@NoArgsConstructor
public class LessonDuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalTime time;
    private String name;

    @OneToMany(mappedBy = "duration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();

    public LessonDuration(LocalTime time, String name) {
        this.time = time;
        this.name = name;
    }

    public void addLesson(Lesson lesson){
        this.lessons.add(lesson);
        lesson.setDuration(this);
    }

    public void removeLesson(Lesson lesson){
        this.lessons.remove(lesson);
        lesson.setDuration(null);
    }
}
