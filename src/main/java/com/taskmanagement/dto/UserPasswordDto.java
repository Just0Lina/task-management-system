package com.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;

@NotBlank
public record UserPasswordDto(
        String email,
        String password) {
}