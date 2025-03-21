package com.taskmanagement.exception;

import com.taskmanagement.dto.Error;
import com.taskmanagement.security.exceptions.AuthorizationException;
import com.taskmanagement.security.exceptions.NoRightsException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@CrossOrigin(maxAge = 1440)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Object> handleBadRequestException(Exception e) {
        logger.error(e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({NoRightsException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(Exception e) {
        logger.error(e.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequest(Exception e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({AuthorizationException.class})
    protected ResponseEntity<Object> handleAuthorizationException(Exception e) {
        String authFailed = "Auth failed: " + e.getMessage();
        logger.error(authFailed);
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, authFailed);
    }

    @ExceptionHandler({ExpiredJwtException.class, MalformedJwtException.class, UnsupportedJwtException.class})
    protected ResponseEntity<Object> handleJwtException(RuntimeException e) {
        String invalidTokenErrMsg = "Invalid token: " + e.getMessage();
        logger.error(invalidTokenErrMsg);
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, invalidTokenErrMsg);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException e) {
        String invalidParamErrMsg = "Error in query: " + e.getMessage();
        logger.error(invalidParamErrMsg);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, invalidParamErrMsg);
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(Error.builder()
                .message(message).code(status.value()).build(), status);
    }

}