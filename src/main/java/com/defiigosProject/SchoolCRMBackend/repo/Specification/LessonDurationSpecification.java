package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.LessonDuration;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;

public class LessonDurationSpecification {

    public static Specification<LessonDuration> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<LessonDuration> withTime(LocalTime time){
        return (root, query, cb) ->
                (time == null || time.toString().isEmpty()) ? null : cb.equal(root.get("time"), time);
    }

    public static Specification<LessonDuration> withName(String name){
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null : cb.equal(root.get("name"), name);
    }
}
