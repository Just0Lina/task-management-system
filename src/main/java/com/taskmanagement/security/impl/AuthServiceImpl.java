package com.taskmanagement.security.impl;
import com.taskmanagement.dto.JwtResponse;
import com.taskmanagement.dto.LoginDto;
import com.taskmanagement.entity.User;
import com.taskmanagement.security.AuthService;
import com.taskmanagement.security.exceptions.AuthorizationException;
import com.taskmanagement.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProviderImpl jwtProviderImpl;

    private static final String WRONG_PASSWORD_MESSAGE = "Wrong password";
    private static final String INVALID_JWT_MESSAGE = "Invalid JWT";
    private static final String PROCESSING_LOGIN_REQUEST = "Processing login request";
    private static final String PROCESSING_GET_ACCESS_TOKEN_REQUEST = "Processing get access token request";
    private static final String PROCESSING_REFRESH_REQUEST = "Processing refresh request";
    private static final String VALIDATING_REFRESH_TOKEN_FAILED = "Validating refresh token failed";

    @Override
    public JwtResponse login(@NonNull LoginDto authRequest) {
        log.info(PROCESSING_LOGIN_REQUEST);
        User user = userService.findUserByEmail(authRequest.email());
        if (passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            return getJwtResponse(user);
        } else {
            log.error(WRONG_PASSWORD_MESSAGE);
            throw new AuthorizationException(WRONG_PASSWORD_MESSAGE);
        }
    }

    private JwtResponse getJwtResponse(User user) {
        final String accessToken = jwtProviderImpl.generateAccessToken(user);
        final String refreshToken = jwtProviderImpl.generateRefreshToken(user);
        refreshStorage.put(String.valueOf(user.getId()), refreshToken);
        return new JwtResponse(accessToken,refreshToken);
    }

    @Override
    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        log.info(PROCESSING_GET_ACCESS_TOKEN_REQUEST);
        if (jwtProviderImpl.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
            final String userId = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(userId);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                User user = userService.findUserById(Long.valueOf(userId));
                final String accessToken = jwtProviderImpl.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        log.error(VALIDATING_REFRESH_TOKEN_FAILED);
        throw new AuthorizationException(INVALID_JWT_MESSAGE);
    }

    @Override
    public JwtResponse refresh(@NonNull String refreshToken) {
        log.info(PROCESSING_REFRESH_REQUEST);
        if (!jwtProviderImpl.validateRefreshToken(refreshToken)) {
            log.error(VALIDATING_REFRESH_TOKEN_FAILED);
            throw new AuthorizationException(INVALID_JWT_MESSAGE);
        }
        final Claims claims = jwtProviderImpl.getRefreshClaims(refreshToken);
        final String userId = claims.getSubject();
        final String saveRefreshToken = refreshStorage.get(userId);
        if (saveRefreshToken == null || !saveRefreshToken.equals(refreshToken)) {
            throw new AuthorizationException(INVALID_JWT_MESSAGE);
        }
        User user = userService.findUserById(Long.valueOf(userId));
        return getJwtResponse(user);
    }
}
