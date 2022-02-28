package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.LessonGroup;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LessonGroupStatusType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class LessonGroupSpecification {
    public static Specification<LessonGroup> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<LessonGroup> withName(String name){
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<LessonGroup> withStatus(LessonGroupStatusType status){
        return (root, query, cb) -> {
            return status == null ? null : cb.equal(root.join("status", JoinType.LEFT).get("status"), status);
        };
    }
}
