package com.taskmanagement.security;


import com.taskmanagement.entity.User;
import io.jsonwebtoken.Claims;
import lombok.NonNull;

/**
 * Интерфейс для работы с JWT токенами.
 */
public interface JwtProvider {
    String generateAccessToken(@NonNull User user);
    String generateRefreshToken(@NonNull User user);
    boolean validateAccessToken(@NonNull String accessToken);
    boolean validateRefreshToken(@NonNull String refreshToken);
    Claims getAccessClaims(@NonNull String token);
    Claims getRefreshClaims(@NonNull String token);
}