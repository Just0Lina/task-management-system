package com.taskmanagement.security;


import com.taskmanagement.dto.UserPasswordDto;
import com.taskmanagement.entity.Task;
import com.taskmanagement.exception.NoRightsException;
import com.taskmanagement.security.impl.JwtAuthentication;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class AuthServiceCommon {
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[^0-9\\s]).*$";
    public static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)" +
            "*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e" +
            "-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2" +
            "(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])" +
            "|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b" +
            "\\x0c\\x0e-\\x7f])+)\\])";
    public static final int PASSWORD_MIN_SYMBOLS = 4;
    public static final int PASSWORD_MAX_SYMBOLS = 32;

    public static void checkRegisterConstraints(UserPasswordDto dto) throws BadRequestException {
        if (!validatePasswordFormat(dto.password())) {
            throw new BadRequestException(
                    "Invalid password, must be  " + PASSWORD_MIN_SYMBOLS + "-" + PASSWORD_MAX_SYMBOLS + " symbols, and contain at least 1 digit and 1 non-digit");
        }
        if (!validateEmailFormat(dto.email())) {
            throw new BadRequestException("Invalid email, must follow rfc 822 & rfc 5322");
        }
    }

    public static boolean validatePasswordFormat(String password) {
        int len = password.length();
        boolean match = password.matches(PASSWORD_PATTERN);
        return len >= PASSWORD_MIN_SYMBOLS && len <= PASSWORD_MAX_SYMBOLS && match;
    }

    public static boolean validateEmailFormat(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public static void checkTaskAccessOrThrow(Task task) {
        if (!hasAccessToTask(task)) {
            throw new NoRightsException();
        }
    }

    public static boolean hasAccessToTask(Task task) {
        return getAuthenticatedUser()
                .map(principal -> hasRole(principal.getAuthorities(), "ADMIN") ||
                        (task.getAssignee() != null && task.getAssignee().getEmail().equals(principal.getEmail())))
                .orElse(false);
    }

    private static Optional<JwtAuthentication> getAuthenticatedUser() {
        return Optional.ofNullable((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication());
    }


    private static boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }

    public static String getCurrentUserEmail() {
        return getAuthenticatedUser()
                .map(JwtAuthentication::getEmail)
                .orElseThrow(() -> new AccessDeniedException("User is not authenticated"));
    }

    public static boolean isUserAdmin() {
        return getAuthenticatedUser()
                .map(principal -> hasRole(principal.getAuthorities(), "ADMIN"))
                .orElse(false);
    }

    public static boolean userHasRightsForTask(Task task) {
        return getAuthenticatedUser()
                .map(principal ->
                        task.getAssignee() != null && task.getAssignee().getEmail().equals(principal.getEmail()))
                .orElse(false);
    }

}