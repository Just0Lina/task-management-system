package com.taskmanagement.security.utils;

import com.taskmanagement.security.impl.JwtAuthentication;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class JwtUtils {

    public static JwtAuthentication generate(Claims claims) {
        JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoleName(claims.get("roles", String.class));
        jwtInfoToken.setUserName(claims.get("username", String.class));
        return jwtInfoToken;
    }
}