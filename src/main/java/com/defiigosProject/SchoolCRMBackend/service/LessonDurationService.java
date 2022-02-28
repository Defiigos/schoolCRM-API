package com.defiigosProject.SchoolCRMBackend.service;

import com.defiigosProject.SchoolCRMBackend.dto.LessonDurationDto;
import com.defiigosProject.SchoolCRMBackend.dto.MessageResponse;
import com.defiigosProject.SchoolCRMBackend.exception.BadRequestException;
import com.defiigosProject.SchoolCRMBackend.model.LessonDuration;
import com.defiigosProject.SchoolCRMBackend.repo.LessonDurationRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.defiigosProject.SchoolCRMBackend.repo.Specification.LessonDurationSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class LessonDurationService {

    private final LessonDurationRepo lessonDurationRepo;

    public LessonDurationService(LessonDurationRepo lessonDurationRepo) {
        this.lessonDurationRepo = lessonDurationRepo;
    }

    public ResponseEntity<MessageResponse> createLessonDuration(LessonDurationDto lessonDurationDto)
            throws BadRequestException {

        if (lessonDurationDto.getTime() == null || lessonDurationDto.getTime().toString().isEmpty()){
            throw new BadRequestException("Error: Time('time') required");
        }

        if (lessonDurationRepo.existsByTime(lessonDurationDto.getTime())){
            throw new BadRequestException("Error: Lesson duration with this Time('time') already exist");
        }

        LessonDuration newLessonDuration = new LessonDuration(lessonDurationDto.getTime(), lessonDurationDto.getName());
        lessonDurationRepo.save(newLessonDuration);
        return ResponseEntity.ok(new MessageResponse("Lesson duration successfully created"));
    }

    public ResponseEntity<List<LessonDurationDto>> getLessonDuration(Long id, LocalTime time, String name) {
        List<LessonDuration> lessonDurationList = lessonDurationRepo.findAll(
                where(withId(id))
                        .and(withTime(time))
                        .and(withName(name))
        );

        List<LessonDurationDto> lessonDurationDtoList = new ArrayList<>();
        for (LessonDuration lessonDuration: lessonDurationList){
            lessonDurationDtoList.add(new LessonDurationDto(
               lessonDuration.getId(),
               lessonDuration.getTime(),
               lessonDuration.getName()
            ));
        }

        return ResponseEntity.ok(lessonDurationDtoList);
    }

    public ResponseEntity<MessageResponse> updateLessonDuration(Long id, LessonDurationDto lessonDurationDto)
            throws BadRequestException {
        Optional<LessonDuration> optionalLessonDuration = lessonDurationRepo.findById(id);

        if (optionalLessonDuration.isEmpty())
            throw new BadRequestException("Error: Lesson duration with this id:" + id + "is not found");
        LessonDuration updatedLessonDuration = optionalLessonDuration.get();



        if (lessonDurationDto.getTime() != null){
            if (lessonDurationDto.getTime().toString().isEmpty())
                throw new BadRequestException("Error: Time('time') must not be empty");
            updatedLessonDuration.setTime(lessonDurationDto.getTime());
        }

        if (lessonDurationDto.getName() != null){
            updatedLessonDuration.setName(lessonDurationDto.getName());
        }

        lessonDurationRepo.save(updatedLessonDuration);
        return ResponseEntity.ok(new MessageResponse("Lesson duration successfully updated"));
    }

    public ResponseEntity<MessageResponse> deleteLessonDuration(Long id) throws BadRequestException {

        Optional<LessonDuration> optionalLessonDuration = lessonDurationRepo.findById(id);

        if (optionalLessonDuration.isEmpty())
            throw new BadRequestException("Error: Lesson duration with this id:" + id + "is not found");
        LessonDuration deletedLessonDuration = optionalLessonDuration.get();

        if (!deletedLessonDuration.getLessonList().isEmpty())
            throw new BadRequestException("This location is used by lessons");

        lessonDurationRepo.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Lesson duration successfully deleted"));
    }
}
