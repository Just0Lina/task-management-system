package com.taskmanagement.security;

import com.taskmanagement.dto.JwtResponse;
import com.taskmanagement.dto.LogoutRequest;
import com.taskmanagement.dto.UserPasswordDto;
import com.taskmanagement.entity.User;
import com.taskmanagement.security.exceptions.AuthorizationException;
import com.taskmanagement.security.impl.AuthServiceImpl;
import com.taskmanagement.security.impl.JwtProviderImpl;
import com.taskmanagement.service.BlacklistService;
import com.taskmanagement.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private JwtProviderImpl jwtProviderImpl;

    @Mock
    private BlacklistService blacklistService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ConcurrentHashMap<String, String> refreshStorage;


    @Test
    void login_ShouldReturnJwtResponse_WhenValidCredentials() {
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";
        UserPasswordDto userPasswordDto = new UserPasswordDto(email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);

        when(userService.findUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtProviderImpl.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtProviderImpl.generateRefreshToken(user)).thenReturn("refreshToken");

        JwtResponse response = authService.login(userPasswordDto);

        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
    }

    @Test
    void login_ShouldThrowAuthorizationException_WhenInvalidPassword() {
        String email = "test@example.com";
        String password = "wrongPassword";
        UserPasswordDto userPasswordDto = new UserPasswordDto(email, password);
        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPassword123");

        when(userService.findUserByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, "encodedPassword123")).thenReturn(false);

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> {
            authService.login(userPasswordDto);
        });

        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void logout_ShouldRemoveTokens_WhenValidRequest() throws AuthException {
        String validRefreshToken = "validRefreshToken";
        String validAccessToken = "validAccessToken";
        refreshStorage.put("userId", validRefreshToken);
        Claims claims = Mockito.mock(Claims.class);
        when(claims.getSubject()).thenReturn("userId");

        when(jwtProviderImpl.validateRefreshToken(validRefreshToken)).thenReturn(true);
        when(jwtProviderImpl.getRefreshClaims(validRefreshToken)).thenReturn(claims);

        LogoutRequest request = new LogoutRequest(validAccessToken, validRefreshToken);

        authService.logout(request);
        assertFalse(refreshStorage.contains("userId"));
        verify(blacklistService, times(1)).addToBlacklist(validAccessToken);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void logout_ShouldNotRemoveTokens_WhenInvalidRequest() {
        String invalidRefreshToken = "invalidRefreshToken";
        String validAccessToken = "validAccessToken";

        when(jwtProviderImpl.validateRefreshToken(invalidRefreshToken)).thenReturn(false);

        LogoutRequest request = new LogoutRequest(validAccessToken, invalidRefreshToken);

        AuthException exception = assertThrows(AuthException.class, () -> {
            authService.logout(request);
        });

        verify(blacklistService, never()).addToBlacklist(anyString());
        verify(refreshStorage, never()).remove("userId");

        assertEquals("Invalid JWT", exception.getMessage());
    }
}