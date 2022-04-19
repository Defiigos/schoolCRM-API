package com.defiigosProject.SchoolCRMBackend.dto.lesson;

import com.defiigosProject.SchoolCRMBackend.dto.LocationDto;
import com.defiigosProject.SchoolCRMBackend.dto.TeacherDto;
import com.defiigosProject.SchoolCRMBackend.model.Lesson;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class LessonDto {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private LessonStatusType status;
    private LessonDurationDto duration;
    private LocationDto location;
    private TeacherDto teacher;
    private LessonGroupDto lessonGroup;

    public static LessonDto build(Lesson lesson){
        return new LessonDto(
                lesson.getId(),
                lesson.getDate(),
                lesson.getTime(),
                lesson.getStatus().getStatus(),
                LessonDurationDto.build(lesson.getDuration()),
                LocationDto.build(lesson.getLocation()),
                TeacherDto.build(lesson.getTeacher()),
                LessonGroupDto.build(lesson.getLessonGroup())
        );
    }
}
