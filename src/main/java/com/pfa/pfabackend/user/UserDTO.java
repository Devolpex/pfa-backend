package com.pfa.pfabackend.user;

import lombok.*;

import java.sql.Date;

import com.pfa.pfabackend.user.enums.Role;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String lastname;
    private String firstname;
    private String email;
    private String phone;
    private String image;
    private Role  role;
    private Date created_at;
    private Long clientId;
    private Long adminId;
    private String password;
    
}



