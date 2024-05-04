package com.pfa.pfabackend.dto.user;

import jakarta.validation.constraints.*;

public class UserCreateDto {
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name must contain only alphabetic characters")
    @NotBlank(message = "Last Name cannot be empty")
    private String last_name;
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only alphabetic characters")
    @NotBlank(message = "First Name cannot be empty")
    private String first_name;
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

    public UserCreateDto(String last_name, String first_name, String email, String phone, String password, String confirm_password) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }
}
