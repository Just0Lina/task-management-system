package com.taskmanagement.security;

import com.taskmanagement.dto.LoginDto;
import com.taskmanagement.dto.JwtResponse;
import lombok.NonNull;


public interface AuthService {
    JwtResponse login(@NonNull LoginDto authRequest);
    JwtResponse getAccessToken(@NonNull String refreshToken);
    JwtResponse refresh(@NonNull String refreshToken);
}