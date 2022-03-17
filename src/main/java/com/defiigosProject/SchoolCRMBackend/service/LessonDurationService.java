package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.lesson.LessonDurationDto;
import com.defiigosProject.SchoolCRMBackend.dto.util.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.extend.*;
import com.defiigosProject.SchoolCRMBackend.model.LessonDuration;
import com.defiigosProject.SchoolCRMBackend.repo.LessonDurationRepo;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.LessonDurationSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class LessonDurationService {

    private final LessonDurationRepo lessonDurationRepo;

    public LessonDurationService(LessonDurationRepo lessonDurationRepo) {
        this.lessonDurationRepo = lessonDurationRepo;
    }

    public ResponseEntity<MessageResponse> createLessonDuration(LessonDurationDto lessonDurationDto)
            throws FieldRequiredException, EntityAlreadyExistException {

        if (lessonDurationDto.getTime() == null || lessonDurationDto.getTime().toString().isEmpty()){
            throw new FieldRequiredException("time");
        }

        if (lessonDurationRepo.existsByTime(lessonDurationDto.getTime())){
            throw new EntityAlreadyExistException("lesson duration");
        }

        LessonDuration newLessonDuration = new LessonDuration(lessonDurationDto.getTime(), lessonDurationDto.getName());
        lessonDurationRepo.save(newLessonDuration);
        return ResponseEntity.ok(new MessageResponse("Lesson duration successfully created"));
    }

    public ResponseEntity<List<LessonDurationDto>> getLessonDuration(Long id, String time, String name)
            throws BadRequestException {

        LocalTime parseTime = null;
        try {
            if (time != null)
                parseTime = LocalTime.parse(time);
        }
        catch (DateTimeParseException e) {
            throw new BadRequestException("time format incorrect");
        }

        List<LessonDuration> lessonDurationList = lessonDurationRepo.findAll(
                where(withId(id))
                        .and(withTime(parseTime))
                        .and(withName(name)),
                Sort.by(Sort.Direction.ASC, "id")
        );

        List<LessonDurationDto> lessonDurationDtoList = new ArrayList<>();
        for (LessonDuration lessonDuration: lessonDurationList){
            lessonDurationDtoList.add(LessonDurationDto.build(lessonDuration));
        }

        return ResponseEntity.ok(lessonDurationDtoList);
    }

    public ResponseEntity<MessageResponse> updateLessonDuration(Long id, LessonDurationDto lessonDurationDto)
            throws EntityNotFoundException, FieldNotNullException, EntityAlreadyExistException, EntityUsedException {

        LessonDuration updatedLessonDuration = lessonDurationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson duration with this id:" + id));


        if (lessonDurationDto.getTime() != null){
            if (lessonDurationDto.getTime().toString().isEmpty())
                throw new FieldNotNullException("time");

            if (!updatedLessonDuration.getTime().equals(lessonDurationDto.getTime())) {
                if (lessonDurationRepo.existsByTime(lessonDurationDto.getTime())) {
                    throw new EntityAlreadyExistException("lesson duration");
                }

                if (!updatedLessonDuration.getLessons().isEmpty())
                    throw new EntityUsedException("lesson duration", "lessons");

                updatedLessonDuration.setTime(lessonDurationDto.getTime());
            }
        }

        if (lessonDurationDto.getName() != null){
            updatedLessonDuration.setName(lessonDurationDto.getName());
        }

        lessonDurationRepo.save(updatedLessonDuration);
        return ResponseEntity.ok(new MessageResponse("Lesson duration successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteLessonDuration(Long id)
            throws EntityNotFoundException, EntityUsedException {

        LessonDuration deletedLessonDuration = lessonDurationRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("lesson duration with this id:" + id));

        if (!deletedLessonDuration.getLessons().isEmpty())
            throw new EntityUsedException("lesson duration", "lessons");

        lessonDurationRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Lesson duration successfully deleted"));
    }
}
