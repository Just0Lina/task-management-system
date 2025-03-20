package com.taskmanagement.service;

import com.taskmanagement.dto.CreateAndUpdateTaskDto;
import com.taskmanagement.dto.TaskDto;
import com.taskmanagement.dto.TaskFilter;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.User;
import com.taskmanagement.mappers.TaskMapper;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.security.AuthServiceCommon;
import com.taskmanagement.security.exceptions.NoRightsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserService userService;

    public TaskDto getTaskById(Long id) {
        Task task = getTaskByIdOrThrow(id);
        AuthServiceCommon.checkTaskAccessOrThrow(task);
        return taskMapper.taskToTaskDto(task);
    }

    @Transactional
    public TaskDto createTask(CreateAndUpdateTaskDto taskDto) {
        Task task = taskMapper.taskDtoToTask(taskDto);
        String email = AuthServiceCommon.getCurrentUserEmail();
        User currentUser = userService.findUserByEmail(email);
        User asignedUser = userService.findUserByEmail(taskDto.getAssigneeEmail());
        task.setAuthor(currentUser);
        task.setAssignee(asignedUser);
        task = taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    @Transactional
    public TaskDto updateTask(Long id, CreateAndUpdateTaskDto taskDto) {
        Task task = getTaskByIdOrThrow(id);
        boolean isUserAdmin = AuthServiceCommon.isUserAdmin();
        if (!isUserAdmin && !AuthServiceCommon.userHasRightsForTask(task)) {
            throw new NoRightsException();
        }
        if (taskDto.getPriority() != null || taskDto.getDescription() != null || taskDto.getTitle() != null || taskDto.getAssigneeEmail() != null) {
            if (!isUserAdmin) {
                throw new NoRightsException();
            }
        }

        taskMapper.updateTaskFromDto(taskDto, task);
        if (taskDto.getAssigneeEmail() != null) {
            User asignedUser = userService.findUserByEmail(taskDto.getAssigneeEmail());
            task.setAssignee(asignedUser);
        }
        task = taskRepository.save(task);
        return taskMapper.taskToTaskDto(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        getTaskByIdOrThrow(id);
        taskRepository.deleteById(id);
    }

    public Task getTaskByIdOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public Page<TaskDto> getTasksWithFilters(TaskFilter filter, Pageable pageable) {
        Specification<Task> spec = Specification.where(null);

        if (filter.getAuthorEmail() != null) {
            spec = spec.and(TaskSpecification.hasAuthorEmail(filter.getAuthorEmail()));
        }

        if (filter.getAssigneeEmail() != null) {
            spec = spec.and(TaskSpecification.hasAssigneeEmail(filter.getAssigneeEmail()));
        }

        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks.map(taskMapper::taskToTaskDto);
    }
}

