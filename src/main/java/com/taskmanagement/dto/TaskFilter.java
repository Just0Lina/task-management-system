package com.taskmanagement.dto;

import lombok.Data;

@Data
public class TaskFilter {
    private String authorEmail;
    private String assigneeEmail;
}
