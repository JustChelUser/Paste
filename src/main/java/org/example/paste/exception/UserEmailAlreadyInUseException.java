package org.example.paste.exception;

public class UserEmailAlreadyInUseException extends RuntimeException {
    public UserEmailAlreadyInUseException(String message) {
        super(message);
    }
}
