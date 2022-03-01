package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lesson_group_statuses")
@Getter
@Setter
@NoArgsConstructor
public class LessonGroupStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 22)
    @Enumerated(EnumType.STRING)
    private LessonGroupStatusType status;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LessonGroup> lessonGroups = new HashSet<>();

    public LessonGroupStatus(LessonGroupStatusType status) {
        this.status = status;
    }

    public void addLessonGroup(LessonGroup lessonGroup){
        this.lessonGroups.add(lessonGroup);
        lessonGroup.setStatus(this);
    }

    public void removeLessonGroup(LessonGroup lessonGroup){
        this.lessonGroups.remove(lessonGroup);
        lessonGroup.setStatus(null);
    }
}
