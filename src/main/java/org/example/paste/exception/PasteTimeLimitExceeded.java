package org.example.paste.exception;

import lombok.Getter;

@Getter
public class PasteTimeLimitExceeded extends RuntimeException {
    public PasteTimeLimitExceeded(String message) {
        super(message);
    }
}
