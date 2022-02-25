package com.defiigosProject.SchoolCRMBackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "request_students")
@Getter
@Setter
@NoArgsConstructor
public class RequestStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    private RequestStudentStatus requestStudentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Location location;

    public RequestStudent(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
}
