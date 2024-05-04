package com.pfa.pfabackend.request.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientCreateRequest {
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name must contain only alphabetic characters")
    @NotBlank(message = "Last Name cannot be empty")
    private String lastname;
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only alphabetic characters")
    @NotBlank(message = "First Name cannot be empty")
    private String firstname;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    @NotBlank(message = "Phone cannot be empty")
    private String phone;
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special symbol, and no whitespace")
    private String password;
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special symbol, and no whitespace")
    private String confirm_password;
}
