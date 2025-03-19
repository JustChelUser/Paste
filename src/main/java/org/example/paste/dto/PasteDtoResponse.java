package org.example.paste.dto;

import lombok.Data;
import org.example.paste.Model.Status;

@Data
public class PasteDtoResponse {
    private final String data;
    private final Status status;
    private final Long id;
    private final String hash;
    private final String username;
    private final Long userId;
}
