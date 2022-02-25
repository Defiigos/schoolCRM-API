package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonGroup> lessonGroupList = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public void addLessonGroup(LessonGroup lessonGroup){
        this.lessonGroupList.add(lessonGroup);
        lessonGroup.setGroup(this);
    }

    public void removeLessonGroup(LessonGroup lessonGroup){
        this.lessonGroupList.remove(lessonGroup);
        lessonGroup.setGroup(null);
    }
}
