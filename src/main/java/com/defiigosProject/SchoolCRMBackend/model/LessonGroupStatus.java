package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "location_statuses")
@Getter
@Setter
@NoArgsConstructor
public class LessonGroupStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private LessonGroupStatusType status;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonGroup> lessonGroupList = new ArrayList<>();

    public LessonGroupStatus(LessonGroupStatusType status) {
        this.status = status;
    }

    public void addLessonGroup(LessonGroup lessonGroup){
        this.lessonGroupList.add(lessonGroup);
        lessonGroup.setStatus(this);
    }

    public void removeLessonGroup(LessonGroup lessonGroup){
        this.lessonGroupList.remove(lessonGroup);
        lessonGroup.setStatus(null);
    }
}
