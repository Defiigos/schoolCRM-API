package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.Student;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.StudentStatusType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class StudentSpecification {

    public static Specification<Student> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Student> withName(String name){
        return (root, query, cb) -> (name == null || name.toString().isEmpty()) ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<Student> withPhone(String phone){
        return (root, query, cb) -> (phone == null || phone.toString().isEmpty()) ? null : cb.equal(root.get("phone"), phone);
    }

    public static Specification<Student> withParentName(String parentName){
        return (root, query, cb) -> (parentName == null || parentName.isEmpty()) ? null : cb.equal(root.get("parentName"), parentName);
    }

    public static Specification<Student> withParentPhone(String parentPhone){
        return (root, query, cb) -> (parentPhone == null || parentPhone.isEmpty()) ? null : cb.equal(root.get("parentPhone"), parentPhone);
    }

    public static Specification<Student> withDescription(String description){
        return (root, query, cb) -> (description == null || description.isEmpty()) ? null : cb.equal(root.get("description"), description);
    }

    public static Specification<Student> withStatus(StudentStatusType status){
        return (root, query, cb) -> {
                return status == null ? null : cb.equal(root.join("status", JoinType.LEFT).get("status"), status);
        };
    }
}
