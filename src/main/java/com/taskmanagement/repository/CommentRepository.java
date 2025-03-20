package com.taskmanagement.repository;

import com.taskmanagement.dto.CommentDto;
import com.taskmanagement.entity.Comment;
import com.taskmanagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    Page<CommentDto> findByTask(Task task, Pageable pageable);
}

