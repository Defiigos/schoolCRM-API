package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "lesson_groups")
@Getter
@Setter
@NoArgsConstructor
public class LessonGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private LessonGroupStatus status;

    @OneToMany(mappedBy = "lessonGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessonList = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "students_groups",
            joinColumns = @JoinColumn(name = "lesson_group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"))
    private Set<Student> students = new HashSet<>();

    public LessonGroup(String name) {
        this.name = name;
    }

    public void addStudent(Student student){
        this.students.add(student);
        student.getLessonGroups().add(this);
    }

    public void removeStudent(Student student){
        this.students.remove(student);
        student.getLessonGroups().remove(this);
    }

    public void addLesson(Lesson lesson) {
        this.lessonList.add(lesson);
        lesson.setLessonGroup(this);
    }

    public void removeLesson(Lesson lesson){
        this.lessonList.remove(lesson);
        lesson.setLessonGroup(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LessonGroup that = (LessonGroup) o;
        return Objects.equals(id, that.id)
                && name.equals(that.name)
                && Objects.equals(status, that.status)
                && Objects.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
