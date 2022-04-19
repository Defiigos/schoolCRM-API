package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.Payment;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.PaymentStatusType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import java.time.LocalDate;
import java.time.LocalTime;

public class PaymentSpecification {
    public static Specification<Payment> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Payment> withLessonId(Long lessonId){
        return (root, query, cb) -> lessonId == null ? null : cb.equal(root.get("lesson"), lessonId);
    }

    public static Specification<Payment> withStudentId(Long studentId){
        return (root, query, cb) -> studentId == null ? null : cb.equal(root.get("student"), studentId);
    }

    public static Specification<Payment> withTeacherId(Long teacherId){
        return (root, query, cb) -> teacherId == null ? null : cb.equal(root.join("lesson", JoinType.LEFT).join("teacher").get("id"), teacherId);
    }

    public static Specification<Payment> withAmountId(Long amountId){
        return (root, query, cb) -> amountId == null ? null : cb.equal(root.get("amount"), amountId);
    }

    public static Specification<Payment> withStudentName(String studentName){
        return (root, query, cb) ->
                (studentName == null || studentName.isEmpty()) ? null :
                        cb.equal(root.join("student", JoinType.LEFT).get("name"), studentName);
    }

    public static Specification<Payment> withTeacherName(String teacherName){
        return (root, query, cb) ->
                (teacherName == null || teacherName.isEmpty()) ? null :
                        cb.equal(root.join("lesson", JoinType.LEFT).join("teacher").get("username"), teacherName);
    }

    public static Specification<Payment> withAmountName(String amountName){
        return (root, query, cb) ->
                (amountName == null || amountName.isEmpty()) ? null :
                        cb.equal(root.join("amount", JoinType.LEFT).get("name"), amountName);
    }

    public static Specification<Payment> withStatus(PaymentStatusType status){
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.join("status", JoinType.LEFT).get("status"), status);
    }

    public static Specification<Payment> withDate(String date){
        return (root, query, cb) -> {
            try {
                return (date == null || date.isEmpty()) ? null : cb.equal(root.get("payDate"), LocalDate.parse(date));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Payment> withDateTo(String dateTo) {
        return (root, query, cb) -> {
            try {
                return dateTo.isEmpty() ? null : cb.lessThanOrEqualTo(root.get("payDate"), LocalDate.parse(dateTo));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Payment> withDateFrom(String dateFrom) {
        return (root, query, cb) -> {
            try {
                return dateFrom.isEmpty() ? null : cb.greaterThanOrEqualTo(root.get("payDate"),  LocalDate.parse(dateFrom));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Payment> withTime(String time){
        return (root, query, cb) -> {
            try {
                return (time == null || time.isEmpty()) ? null : cb.equal(root.get("payTime"), LocalTime.parse(time));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Payment> withTimeTo(String timeTo) {
        return (root, query, cb) -> {
            try {
                return timeTo.isEmpty() ? null : cb.lessThanOrEqualTo(root.get("payTime"), LocalTime.parse(timeTo));
            } catch (Exception e) {
                return null;
            }
        };
    }

    public static Specification<Payment> withTimeFrom(String timeFrom) {
        return (root, query, cb) -> {
            try {
                return timeFrom.isEmpty() ? null : cb.greaterThan(root.get("payTime"),  LocalTime.parse(timeFrom));
            } catch (Exception e) {
                return null;
            }
        };
    }
}
