package com.trainticketbooking.app.Exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR, false),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST, false),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST, false),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST, false),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST, false),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND, false),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED, false),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN, false),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST, false),
    USER_ALREADY_EXISTS(1009, "Email with this Email already exists", HttpStatus.BAD_REQUEST, false),
    PASSWORDS_DO_NOT_MATCH(1010, "Passwords do not match", HttpStatus.BAD_REQUEST, false),
    EMAIL_SEND_FAILED(1011, "Failed to send the email", HttpStatus.INTERNAL_SERVER_ERROR, false),
    INVALID_TOKEN(1012, "The token is invalid or expired", HttpStatus.BAD_REQUEST, false),
    TOKEN_EXPIRED(1013, "The token has expired", HttpStatus.BAD_REQUEST, false),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode, boolean success) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
        this.success = success;
    }

    private final int code;
    private final String message;
    private final boolean success;
    private final HttpStatusCode statusCode;
}
