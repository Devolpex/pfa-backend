package com.pfa.pfabackend.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeValidationRequest {
    @Pattern(regexp = "\\d{6}", message = "Code must contain 6 digits")
    private String code;
    @Email(message = "Check if your email exist")
    @NotBlank(message = "Email dont be empty")
    private String email;
}
