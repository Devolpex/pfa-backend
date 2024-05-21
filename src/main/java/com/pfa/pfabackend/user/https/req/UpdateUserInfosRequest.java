package com.pfa.pfabackend.user.https.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserInfosRequest {

    @NotNull(message = "Id cannot be null")
    private Long id;
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

}
