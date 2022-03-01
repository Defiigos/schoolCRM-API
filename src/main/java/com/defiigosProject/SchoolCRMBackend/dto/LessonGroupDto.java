package com.defiigosProject.SchoolCRMBackend.dto;

import com.defiigosProject.SchoolCRMBackend.model.LessonGroup;
import com.defiigosProject.SchoolCRMBackend.model.Student;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class LessonGroupDto {
    private Long id;
    private String name;
    private LessonGroupStatusType status;
    private Set<StudentDto> students;

    public static LessonGroupDto build(LessonGroup lessonGroup){
        Set<StudentDto> studentDtos = new HashSet<>();
        for (Student student: lessonGroup.getStudents()){
            studentDtos.add(StudentDto.build(student));
        }

        return new LessonGroupDto(
                lessonGroup.getId(),
                lessonGroup.getName(),
                lessonGroup.getStatus().getStatus(),
                studentDtos
        );
    }
}
