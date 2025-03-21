package com.taskmanagement.controller;

import com.taskmanagement.dto.CreateAndUpdateTaskDto;
import com.taskmanagement.dto.TaskDto;
import com.taskmanagement.dto.TaskFilter;
import com.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public TaskDto createTask(@Valid @RequestBody CreateAndUpdateTaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/{taskId}")
    public TaskDto getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PutMapping("/{taskId}")
    public TaskDto updateTask(@PathVariable Long taskId, @Valid @RequestBody CreateAndUpdateTaskDto taskDto) {
        return taskService.updateTask(taskId, taskDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public Page<TaskDto> getTasks(
            TaskFilter filter,
            @Parameter(
                    example = "{ \"page\": 0, \"size\": 10, \"sort\": \"title,asc\" }")
            @PageableDefault(size = 20) Pageable pageable) {
        return taskService.getTasksWithFilters(filter, pageable);
    }
}
