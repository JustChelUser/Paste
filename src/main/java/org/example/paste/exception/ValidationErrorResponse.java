package org.example.paste.exception;

public class ValidationErrorResponse extends ErrorResponse {
    private final String field;

    public ValidationErrorResponse(String message, String field) {
        super(message);
        this.field = field;
    }
}
