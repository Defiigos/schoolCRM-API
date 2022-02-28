package com.defiigosProject.SchoolCRMBackend.repo.Specification;

import com.defiigosProject.SchoolCRMBackend.model.PaymentAmount;
import org.springframework.data.jpa.domain.Specification;

public class PaymentAmountSpecification {
    public static Specification<PaymentAmount> withId(Long id){
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<PaymentAmount> withSum(Float sum){
        return (root, query, cb) -> (sum == null || sum.toString().isEmpty()) ? null : cb.equal(root.get("sum"), sum);
    }

    public static Specification<PaymentAmount> withName(String name){
        return (root, query, cb) -> (name == null || name.isEmpty()) ? null : cb.equal(root.get("name"), name);
    }
}
