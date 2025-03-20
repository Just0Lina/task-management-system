package com.taskmanagement.service;

import com.taskmanagement.TestAuthenticationHelper;
import com.taskmanagement.dto.CreateAndUpdateTaskDto;
import com.taskmanagement.dto.TaskDto;
import com.taskmanagement.dto.TaskFilter;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskPriority;
import com.taskmanagement.entity.User;
import com.taskmanagement.mappers.TaskMapper;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.security.exceptions.NoRightsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User author;
    private User assignee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new User();
        author.setEmail("admin@user.com");

        assignee = new User();
        assignee.setEmail("test@user.com");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Task Description");
        task.setPriority(TaskPriority.LOW);
        task.setAuthor(author);
        task.setAssignee(assignee);
    }


    @Test
    void getTaskById_TaskExists_ReturnsTaskById() {
        TestAuthenticationHelper.setUserAuthentication();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.taskToTaskDto(any())).thenReturn(TaskDto.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Task Description")
                .build());

        TaskDto taskDto = taskService.getTaskById(1L);

        assertNotNull(taskDto);
        assertEquals("Test Task", taskDto.getTitle());
    }

    @Test
    void createTask_ValidCreateDtoProvided_CreatesTaskSuccessfully() {
        CreateAndUpdateTaskDto createTaskDto = CreateAndUpdateTaskDto.builder()
                .title("New Task")
                .description("New Task Description")
                .priority(TaskPriority.MEDIUM.name())
                .assigneeEmail("assignee@user.com")
                .build();
        TestAuthenticationHelper.setAdminAuthentication();

        when(taskMapper.taskDtoToTask(createTaskDto)).thenReturn(task);
        when(userService.findUserByEmail("author@user.com")).thenReturn(author);
        when(userService.findUserByEmail("assignee@user.com")).thenReturn(assignee);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.taskToTaskDto(task)).thenReturn(TaskDto.builder()
                .id(1L)
                .title("New Task")
                .description("New Task Description")
                .build());

        TaskDto createdTask = taskService.createTask(createTaskDto);

        assertNotNull(createdTask);
        assertEquals("New Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_ValidUpdateDtoProvided_UpdatesTaskSuccessfully() {
        TestAuthenticationHelper.setAdminAuthentication();
        CreateAndUpdateTaskDto updateTaskDto = CreateAndUpdateTaskDto.builder()
                .title("Updated Task")
                .description("Updated Task Description")
                .priority(TaskPriority.HIGH.name())
                .assigneeEmail(TestAuthenticationHelper.adminEmail)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userService.findUserByEmail(TestAuthenticationHelper.adminEmail)).thenReturn(assignee);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.taskToTaskDto(task)).thenReturn(TaskDto.builder()
                .id(1L)
                .title("Updated Task")
                .description("Updated Task Description")
                .build());

        TaskDto updatedTask = taskService.updateTask(1L, updateTaskDto);

        assertNotNull(updatedTask);
        assertEquals("Updated Task", updatedTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_NoRightsToUpdateTask_ThrowsNoRightsException() {
        TestAuthenticationHelper.clearAuthentication();
        CreateAndUpdateTaskDto updateTaskDto = CreateAndUpdateTaskDto.builder()
                .title("Updated Task")
                .description("Updated Task Description")
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(NoRightsException.class, () -> taskService.updateTask(1L, updateTaskDto));
    }

    @Test
    void deleteTask_TaskExists_DeletesTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_TaskNotFound_ThrowsIllegalArgumentException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void getTasksWithFilters_FilterApplied_ReturnsFilteredTasks() {
        TaskFilter filter = TaskFilter.builder()
                .assigneeEmail("assignee@user.com")
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Task> taskPage = new PageImpl<>(Collections.singletonList(task));
        when(taskRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(taskPage);
        when(taskMapper.taskToTaskDto(any())).thenReturn(TaskDto.builder()
                .id(1L)
                .title("Filtered Task")
                .description("Filtered Task Description")
                .build());

        Page<TaskDto> tasks = taskService.getTasksWithFilters(filter, pageable);

        assertNotNull(tasks);
        assertEquals(1, tasks.getContent().size());
        assertEquals("Filtered Task", tasks.getContent().get(0).getTitle());
    }
}
