package com.taskmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {

    @NotNull
    private String title;
    private String description;
    private String status;
    private String priority;
    private String comments;
}