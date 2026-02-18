package com.compras.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String timestamp;
    private int status;
    private String code;
    private String message;
    private Map<String, String> fieldErrors;

    public static ErrorResponse of(int status, String code, String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status)
                .code(code)
                .message(message)
                .build();
    }

    public static ErrorResponse withFieldErrors(int status, String code, String message, Map<String, String> fieldErrors) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status)
                .code(code)
                .message(message)
                .fieldErrors(fieldErrors)
                .build();
    }
}
