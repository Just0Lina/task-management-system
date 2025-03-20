package com.taskmanagement;

import com.taskmanagement.security.impl.JwtAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestAuthenticationHelper {

    public static String userEmail = "test@user.com";
    public static String adminEmail = "admin@user.com";

    public static void setUserAuthentication() {
        JwtAuthentication authentication = new JwtAuthentication(true, userEmail, "USER");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void setAdminAuthentication() {
        JwtAuthentication authentication = new JwtAuthentication(true, adminEmail, "ADMIN");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }
}
