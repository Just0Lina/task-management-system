package com.taskmanagement.controller;


import com.taskmanagement.dto.JwtResponse;
import com.taskmanagement.dto.LogoutRequest;
import com.taskmanagement.dto.RefreshJwtRequest;
import com.taskmanagement.dto.UserPasswordDto;
import com.taskmanagement.security.AuthService;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody UserPasswordDto authRequest) {
        final JwtResponse token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<JwtResponse> getNewRefresh(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.refresh(request.refreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/auth/token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        final JwtResponse token = authService.getAccessToken(request.refreshToken());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtResponse> register(@RequestBody UserPasswordDto user) throws AuthException, BadRequestException {
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest request) throws AuthException {
        authService.logout(request);
    }

}