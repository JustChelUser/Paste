package org.example.paste.dto;

import lombok.Data;
import org.example.paste.Model.Status;

@Data
public class PasteDtoResponse {
    private final String data;
    private final Status status;
}
