package org.example.paste.exception;
import lombok.Getter;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private final String field;
    public ValidationErrorResponse(String message, String field) {
        super(message);
        this.field = field;
    }
}
