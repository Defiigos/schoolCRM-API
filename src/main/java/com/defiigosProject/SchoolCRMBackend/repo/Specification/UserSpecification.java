package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.Role;
import com.defiigosProject.SchoolCRMBackend.model.User;
import com.defiigosProject.SchoolCRMBackend.model.enumerated.RoleType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Set;

public class UserSpecification {
    public static Specification<User> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<User> withName(String name){
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null : cb.equal(root.get("username"), name);
    }

    public static Specification<User> withEmail(String email){
        return (root, query, cb) ->
                (email == null || email.isEmpty()) ? null : cb.equal(root.get("email"), email);
    }


    public static Specification<User> hasRole(RoleType findRole) {
        return (root, query, cb) -> {
            if (findRole == null) return null;

            query.distinct(true);
            Subquery<Role> roleSubQuery = query.subquery(Role.class);
            Root<Role> role = roleSubQuery.from(Role.class);
            Expression<Collection<Role>> roleUsers = root.get("roles");
            roleSubQuery.select(role);
            roleSubQuery.where(cb.equal(role.get("name"), findRole), cb.isMember(role, roleUsers));
            return cb.exists(roleSubQuery);
        };
    }

    public static Specification<User> withActive(Boolean active){
        return (root, query, cb) ->
                active == null ? null : cb.equal(root.get("isActive"), active);
    }
}
