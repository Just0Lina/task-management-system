package com.taskmanagement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateAndUpdateTaskDto {
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assigneeEmail;
}