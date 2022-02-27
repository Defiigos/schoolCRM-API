package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
@Getter
@Setter
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String address;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private LocationStatus status;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessonList = new ArrayList<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestStudent> requestStudentList = new ArrayList<>();

    public Location(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public void addLesson(Lesson lesson){
        this.lessonList.add(lesson);
        lesson.setLocation(this);
    }

    public void removeLesson(Lesson lesson){
        this.lessonList.remove(lesson);
        lesson.setLocation(null);
    }

    public void addRequestStudent(RequestStudent requestStudent){
        this.requestStudentList.add(requestStudent);
        requestStudent.setLocation(this);
    }

    public void removeRequestStudent(RequestStudent requestStudent){
        this.requestStudentList.remove(requestStudent);
        requestStudent.setLocation(null);
    }


}
