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
        return (root, query, cb) -> name == null ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<RequestStudent> withPhone(String phone){
        return (root, query, cb) -> phone == null ? null : cb.equal(root.get("phone"), phone);
    }

    public static Specification<RequestStudent> withStatus(String status){
        return (root, query, cb) -> {
            try {
                return status == null ? null :
                        cb.equal(root.join("requestStudentStatus", JoinType.LEFT).get("status"), RequestStudentStatusType.valueOf(status));
            } catch (IllegalArgumentException e){
                return null;
            }
        };
    }

    public static Specification<RequestStudent> withLocationId(Long locationId){
        return (root, query, cb) -> locationId == null ? null : cb.equal(root.get("location_id"), locationId);
    }
}
