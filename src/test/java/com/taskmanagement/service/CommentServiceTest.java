package com.taskmanagement.service;

import com.taskmanagement.TestAuthenticationHelper;
import com.taskmanagement.dto.CommentDto;
import com.taskmanagement.entity.Comment;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.mappers.CommentMapper;
import com.taskmanagement.repository.CommentRepository;
import com.taskmanagement.security.exceptions.NoRightsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private TaskService taskService;

    @Mock
    private UserService userService;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Task task;
    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        task = new Task();
        task.setId(1L);

        user = new User();
        user.setEmail("test@user.com");

        comment = new Comment();
        comment.setContent("Test comment");
        comment.setTask(task);
        comment.setUser(user);

        task.setAssignee(user);
        task.setComments(new ArrayList<>());
        Collections.addAll(task.getComments(), comment);
    }

    @Test
    void shouldAllowAddingCommentWhenUserIsAssignee() {
        TestAuthenticationHelper.setUserAuthentication();

        when(taskService.getTaskByIdOrThrow(1L)).thenReturn(task);
        when(userService.findUserByEmail("test@user.com")).thenReturn(user);

        commentService.addComment(1L, "Test comment");

        verify(taskService).saveTask(task);
        assertTrue(task.getComments().contains(comment), "The comment should be added to the task.");
    }

    @Test
    void shouldReturnTaskCommentsSuccessfully() {
        TestAuthenticationHelper.setUserAuthentication();

        when(taskService.getTaskByIdOrThrow(1L)).thenReturn(task);
        when(commentMapper.commentToCommentDto(any())).thenReturn(new CommentDto("Test comment", "test@user.com"));

        List<CommentDto> comments = commentService.getTaskComments(1L);

        assertNotNull(comments);
        assertEquals(1, comments.size(), "The task should contain one comment.");
        assertEquals("Test comment", comments.get(0).content(), "The comment content should match.");
    }

    @Test
    void shouldReturnCommentsByTaskId() {
        TestAuthenticationHelper.setUserAuthentication();

        Pageable pageable = Pageable.unpaged();
        Page<CommentDto> page = new PageImpl<>(Arrays.asList(new CommentDto("Test comment", "test@user.com")));

        when(taskService.getTaskByIdOrThrow(1L)).thenReturn(task);
        when(commentRepository.findByTask(task, pageable)).thenReturn(page);

        Page<CommentDto> result = commentService.getCommentsByTaskId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size(), "The page should contain one comment.");
        assertEquals("Test comment", result.getContent().get(0).content(), "The comment content should match.");
    }

    @Test
    void shouldThrowNoRightsExceptionWhenUserIsNotAssignee() {
        TestAuthenticationHelper.setUserAuthentication();

        User anotherUser = new User();
        anotherUser.setEmail("other@user.com");
        task.setAssignee(anotherUser);

        when(taskService.getTaskByIdOrThrow(1L)).thenReturn(task);
        when(userService.findUserByEmail("test@user.com")).thenReturn(user);

        assertThrows(NoRightsException.class, () -> commentService.addComment(1L, "Test comment"),
                "User should not be allowed to add a comment to a task they are not assigned to.");
    }

    @Test
    void shouldThrowNoRightsExceptionWhenFetchingAnotherUsersTaskComments() {
        TestAuthenticationHelper.setUserAuthentication();

        User anotherUser = new User();
        anotherUser.setEmail("other@user.com");
        task.setAssignee(anotherUser);

        when(taskService.getTaskByIdOrThrow(1L)).thenReturn(task);
        when(commentMapper.commentToCommentDto(any())).thenReturn(new CommentDto("Test comment", "test@user.com"));

        assertThrows(NoRightsException.class, () -> commentService.getTaskComments(1L),
                "User should not be allowed to fetch comments of a task they are not assigned to.");
    }

    @Test
    void shouldThrowNoRightsExceptionWhenFetchingCommentsByTaskIdForAnotherUsersTask() {
        TestAuthenticationHelper.setUserAuthentication();

        Pageable pageable = Pageable.unpaged();
        Page<CommentDto> page = new PageImpl<>(Arrays.asList(new CommentDto("Test comment", "test@user.com")));

        User anotherUser = new User();
        anotherUser.setEmail("other@user.com");
        task.setAssignee(anotherUser);

        when(taskService.getTaskByIdOrThrow(1L)).thenReturn(task);
        when(commentRepository.findByTask(task, pageable)).thenReturn(page);

        assertThrows(NoRightsException.class, () -> commentService.getCommentsByTaskId(1L, pageable),
                "User should not be allowed to fetch comments of a task they are not assigned to.");
    }
}
