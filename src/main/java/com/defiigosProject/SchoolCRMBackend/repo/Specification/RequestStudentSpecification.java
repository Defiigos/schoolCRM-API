package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.RequestStudent;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class RequestStudentSpecification {

    public static Specification<RequestStudent> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<RequestStudent> withName(String name){
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<RequestStudent> withPhone(String phone){
        return (root, query, cb) -> (phone == null || phone.isEmpty()) ? null : cb.equal(root.get("phone"), phone);
    }

    public static Specification<RequestStudent> withStatus(RequestStudentStatusType status){
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.join("status", JoinType.LEFT).get("status"), status);
    }

    public static Specification<RequestStudent> withLocationId(Long locationId){
        return (root, query, cb) ->
                locationId == null ? null : cb.equal(root.join("location", JoinType.LEFT).get("id"), locationId);
    }
}
