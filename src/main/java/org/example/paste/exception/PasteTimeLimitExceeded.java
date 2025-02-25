package org.example.paste.exception;

public class PasteTimeLimitExceeded extends RuntimeException {
    public PasteTimeLimitExceeded(String message) {
        super(message);
    }
}
