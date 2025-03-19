package com.taskmanagement.dto;

import lombok.Data;

/**
 * DTO для ответа JWT при авторизации.
 */
public record JwtResponse(String accessToken, String refreshToken) {}