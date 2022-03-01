package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.User;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;

public class UserSpecification {
    public static Specification<User> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<User> withName(String name){
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null : cb.equal(root.get("name"), name);
    }

    public static Specification<User> withEmail(String email){
        return (root, query, cb) ->
                (email == null || email.isEmpty()) ? null : cb.equal(root.get("email"), email);
    }

    public static Specification<User> withRole(RoleType role){
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.join("roles", JoinType.LEFT).get("name"), role);
    }
}
