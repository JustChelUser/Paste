package org.example.paste.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank(message = "username can't be blank")
    @Size(min = 3, max = 30, message = "username must be between 3 and 30 symbols")
    private String username;
    @NotBlank(message = "email can't be blank")
    @Email(message = "email not correct")
    private String email;
    @NotBlank(message = "password can't be blank")
    @Size(min = 8, max = 30, message = "password must be between 8 and 30 symbols")
    private String password;
}
