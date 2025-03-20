package com.taskmanagement.mappers;

import com.taskmanagement.dto.CreateAndUpdateTaskDto;
import com.taskmanagement.dto.TaskDto;
import com.taskmanagement.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "title", expression = "java(taskDto.getTitle() != null ? taskDto.getTitle() : task.getTitle())")
    @Mapping(target = "description", expression = "java(taskDto.getDescription() != null ? taskDto.getDescription() : task.getDescription())")
    @Mapping(target = "status", expression = "java(taskDto.getStatus() != null ? TaskStatus.valueOf(taskDto.getStatus()) : task.getStatus())")
    @Mapping(target = "priority", expression = "java(taskDto.getPriority() != null ? TaskPriority.valueOf(taskDto.getPriority()) : task.getPriority())")
    void updateTaskFromDto(CreateAndUpdateTaskDto taskDto, @MappingTarget Task task);

    @Mapping(source = "status", target = "status")
    @Mapping(source = "priority", target = "priority")
    @Mapping(source = "assignee.email", target = "assigneeEmail")
    TaskDto taskToTaskDto(Task task);

    @Mapping(source = "status", target = "status")
    @Mapping(source = "priority", target = "priority")
    Task taskDtoToTask(CreateAndUpdateTaskDto taskDto);
}
