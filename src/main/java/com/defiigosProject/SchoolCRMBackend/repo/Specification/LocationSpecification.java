package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.Location;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.LocationStatusType;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RequestStudentStatusType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class LocationSpecification {
    public static Specification<Location> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Location> withAddress(String address){
        return (root, query, cb) -> (address == null || address.isEmpty()) ? null : cb.equal(root.get("address"), address);
    }

    public static Specification<Location> withName(String name){
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<Location> withStatus(String status){
        return (root, query, cb) -> {
            try {
                return status == null ? null :
                        cb.equal(root.join("locationStatus", JoinType.LEFT).get("status"), LocationStatusType.valueOf(status));
            } catch (IllegalArgumentException e){
                return null;
            }
        };
    }
}
