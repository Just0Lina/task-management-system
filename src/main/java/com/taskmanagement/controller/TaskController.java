package com.taskmanagement.controller;

import com.taskmanagement.dto.TaskDto;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @PostMapping
    public TaskDto createTask(@Valid @RequestBody TaskDto taskDTO) {
        throw new IllegalArgumentException("Not implemented");    }

    @GetMapping
    public List<TaskDto> getAllTasks() {
        throw new IllegalArgumentException("Not implemented");    }

    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        throw new IllegalArgumentException("Not implemented");    }

    @PutMapping("/{id}")
    public TaskDto updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDTO) {
        throw new IllegalArgumentException("Not implemented");    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        throw new IllegalArgumentException("Not implemented");    }
}
