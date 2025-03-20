package com.taskmanagement.service;

import com.taskmanagement.dto.CommentDto;
import com.taskmanagement.entity.Comment;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.mappers.CommentMapper;
import com.taskmanagement.repository.CommentRepository;
import com.taskmanagement.security.AuthServiceCommon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final TaskService taskService;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Transactional
    public void addComment(Long taskId, String content) {
        Task task = taskService.getTaskByIdOrThrow(taskId);
        AuthServiceCommon.checkTaskAccessOrThrow(task);

        String email = AuthServiceCommon.getCurrentUserEmail();
        User currentUser = userService.findUserByEmail(email);
        Comment comment = Comment.builder().content(content).task(task).user(currentUser).build();
        task.getComments().add(comment);
        taskService.saveTask(task);
    }

    public List<CommentDto> getTaskComments(Long taskId) {
        Task task = taskService.getTaskByIdOrThrow(taskId);
        AuthServiceCommon.checkTaskAccessOrThrow(task);
        return task.getComments().stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    public Page<CommentDto> getCommentsByTaskId(Long taskId, Pageable pageable) {
        Task task = taskService.getTaskByIdOrThrow(taskId);
        AuthServiceCommon.checkTaskAccessOrThrow(task);
        return commentRepository.findByTask(task, pageable);
    }
}
