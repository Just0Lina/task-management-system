package com.taskmanagement.dto;

public record LogoutRequest(String accessToken, String refreshToken) {
}