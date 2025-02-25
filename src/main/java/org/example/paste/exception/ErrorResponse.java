package org.example.paste.exception;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private final String message;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
