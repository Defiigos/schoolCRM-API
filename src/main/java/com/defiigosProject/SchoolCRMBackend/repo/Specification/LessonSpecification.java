package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.Lesson;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonStatusType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import java.time.LocalDate;
import java.time.LocalTime;


public class LessonSpecification {
    public static Specification<Lesson> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Lesson> withDate(String date){
        return (root, query, cb) -> {
            try {
                return (date == null || date.isEmpty()) ? null : cb.equal(root.get("date"), LocalDate.parse(date));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Lesson> withTime(String time){
        return (root, query, cb) -> {
            try {
                return (time == null || time.isEmpty()) ? null : cb.equal(root.get("time"), LocalTime.parse(time));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Lesson> withStatus(LessonStatusType status){
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.join("status", JoinType.LEFT).get("status"), status);
    }

    public static Specification<Lesson> withTeacherId(Long teacherId){
        return (root, query, cb) ->
                teacherId == null ? null : cb.equal(root.join("teacher", JoinType.LEFT).get("id"), teacherId);
    }

    public static Specification<Lesson> withDurationId(Long durationId){
        return (root, query, cb) ->
                durationId == null ? null : cb.equal(root.join("duration", JoinType.LEFT).get("id"), durationId);
    }

    public static Specification<Lesson> withLocationId(Long locationId){
        return (root, query, cb) ->
                locationId == null ? null : cb.equal(root.join("location", JoinType.LEFT).get("id"), locationId);
    }

    public static Specification<Lesson> withLessonGroupId(Long lessonGroupId){
        return (root, query, cb) ->
                lessonGroupId == null ? null :
                        cb.equal(root.join("lessonGroup", JoinType.LEFT).get("id"), lessonGroupId);
    }

    public static Specification<Lesson> withDateFrom(String dateFrom) {
        return (root, query, cb) -> {
            try {
                return dateFrom.isEmpty() ? null : cb.greaterThan(root.get("date"),  LocalDate.parse(dateFrom));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Lesson> withDateTo(String dateTo) {
        return (root, query, cb) -> {
            try {
                return dateTo.isEmpty() ? null : cb.lessThanOrEqualTo(root.get("date"), LocalDate.parse(dateTo));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Lesson> withTimeFrom(String timeFrom) {
        return (root, query, cb) -> {
            try {
                return timeFrom.isEmpty() ? null : cb.greaterThan(root.get("time"),  LocalTime.parse(timeFrom));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Lesson> withTimeTo(String timeTo) {
        return (root, query, cb) -> {
            try {
                return timeTo.isEmpty() ? null : cb.lessThanOrEqualTo(root.get("time"), LocalTime.parse(timeTo));
            } catch (Exception e) {
                return null;
            }
        };
    }
}
