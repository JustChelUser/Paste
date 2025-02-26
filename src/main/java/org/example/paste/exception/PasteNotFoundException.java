package org.example.paste.exception;

import lombok.Getter;

@Getter
public class PasteNotFoundException extends RuntimeException {
    public PasteNotFoundException(String message) {
        super(message);
    }
}
