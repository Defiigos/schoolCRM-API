package com.defiigosProject.SchoolCRMBackend.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
    })
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(nullable = false)
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessonList = new ArrayList<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addLesson(Lesson lesson){
        this.lessonList.add(lesson);
        lesson.setTeacher(this);
    }

    public void removeLesson(Lesson lesson){
        this.lessonList.remove(lesson);
        lesson.setTeacher(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}