package org.example.paste.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.example.paste.Model.Status;

@Data
public class PasteDtoRequest {
    @NotBlank(message = "data cannot be blank")
    @Size(max = 5000, message = "data cannot be more 5000 symbols")
    private final String data;
    @NotNull(message = "status cannot be null")
    private final Status status;
    @Min(value = 1, message = "Time to live must be at least 1 second")
    @Max(value = 2592000, message = "Time to live cannot exceed 30 days")
    private final long timeToLiveSeconds;
}
