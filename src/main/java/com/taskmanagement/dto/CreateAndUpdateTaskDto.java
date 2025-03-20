package com.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAndUpdateTaskDto {
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assigneeEmail;
}