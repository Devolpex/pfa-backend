package com.pfa.pfabackend.user.https.req;

import com.pfa.pfabackend.user.validation.PasswordConstraint;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePasswordRequest {
    @NotBlank
    private String token;
    @PasswordConstraint
    private String oldPassword;
    @PasswordConstraint
    private String newPassword;
    @PasswordConstraint
    private String confirmNewPassword;
}
