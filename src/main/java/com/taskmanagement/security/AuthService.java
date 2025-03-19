package com.taskmanagement.security;

import com.taskmanagement.dto.JwtResponse;
import com.taskmanagement.dto.LogoutRequest;
import com.taskmanagement.dto.UserPasswordDto;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import org.apache.coyote.BadRequestException;


public interface AuthService {
    JwtResponse login(@NonNull UserPasswordDto authRequest);
    JwtResponse getAccessToken(@NonNull String refreshToken);
    JwtResponse refresh(@NonNull String refreshToken);

    JwtResponse register(@NonNull UserPasswordDto userDto) throws AuthException, BadRequestException;

    void logout(@NonNull LogoutRequest refreshToken) throws AuthException;
}