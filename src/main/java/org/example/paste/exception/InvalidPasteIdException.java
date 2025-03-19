package org.example.paste.exception;

public class InvalidPasteIdException extends RuntimeException {
    public InvalidPasteIdException(String message) {
        super(message);
    }
}
