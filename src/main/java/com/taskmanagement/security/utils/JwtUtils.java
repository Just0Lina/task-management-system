package com.taskmanagement.security.utils;

import com.taskmanagement.security.impl.JwtAuthentication;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoleName(claims.get("roles", String.class));
        jwtInfoToken.setEmail(claims.get("email", String.class));
        return jwtInfoToken;
    }
}