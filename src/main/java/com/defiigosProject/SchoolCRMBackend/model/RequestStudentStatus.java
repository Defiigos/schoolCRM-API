package com.defiigosProject.SchoolCRMBackend.model;

import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "request_student_statuses")
@Getter
@Setter
@NoArgsConstructor
public class RequestStudentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RequestStudentStatusType status;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestStudent> requestStudentList = new ArrayList<>();

    public RequestStudentStatus(RequestStudentStatusType status) {
        this.status = status;
    }

    public void addRequestStudent(RequestStudent requestStudent){
        this.requestStudentList.add(requestStudent);
        requestStudent.setStatus(this);
    }

    public void removeRequestStudent(RequestStudent requestStudent){
        this.requestStudentList.remove(requestStudent);
        requestStudent.setStatus(null);
    }
}
