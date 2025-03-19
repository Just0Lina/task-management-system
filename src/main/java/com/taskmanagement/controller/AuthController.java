package com.taskmanagement.controller;


import com.taskmanagement.dto.JwtResponse;
import com.taskmanagement.dto.LoginDto;
import com.taskmanagement.dto.RefreshJwtRequest;
import com.taskmanagement.security.AuthService;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<JwtResponse> login(@RequestBody LoginDto authRequest) {
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

}