package com.taskmanagement.service;

import com.taskmanagement.entity.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasAuthorEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("author").get("email"), email);
    }

    public static Specification<Task> hasAssigneeEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("assignee").get("email"), email);
    }

}
