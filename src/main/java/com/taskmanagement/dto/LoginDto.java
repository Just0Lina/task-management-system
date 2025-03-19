package com.taskmanagement.dto;

import lombok.Getter;
import lombok.Setter;

public record LoginDto(
        String email,
        String password) {
}