package com.taskmanagement.controller;

import com.taskmanagement.dto.CommentDto;
import com.taskmanagement.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping
    public void addComment(@PathVariable Long taskId, @RequestParam String content) {
        commentService.addComment(taskId, content);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public Page<CommentDto> getTaskComments(
            @PathVariable Long taskId,
            @PageableDefault(size = 20) Pageable pageable) {
        return commentService.getCommentsByTaskId(taskId, pageable);
    }

}
