package com.taskmanagement;


import com.taskmanagement.dto.UserPasswordDto;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

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
        if (!isPasswordValid(dto.password())) {
            throw new BadRequestException(
                    "Invalid password, must be  " + PASSWORD_MIN_SYMBOLS + "-" + PASSWORD_MAX_SYMBOLS + " symbols, and contain at least 1 digit and 1 non-digit");
        }
        if (!isEmailValid(dto.email())) {
            throw new BadRequestException("Invalid email, must follow rfc 822 & rfc 5322");
        }
    }

    public static boolean isPasswordValid(String password) {
        int len = password.length();
        boolean match = password.matches(PASSWORD_PATTERN);
        return len >= PASSWORD_MIN_SYMBOLS && len <= PASSWORD_MAX_SYMBOLS && match;
    }

    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN);
    }
}