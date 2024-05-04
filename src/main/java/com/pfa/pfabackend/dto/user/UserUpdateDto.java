package com.pfa.pfabackend.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data

public class UserUpdateDto {
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name must contain only alphabetic characters")
    @NotBlank(message = "Last Name cannot be empty")
    private String last_name;
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only alphabetic characters")
    @NotBlank(message = "First Name cannot be empty")
    private String first_name;
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
