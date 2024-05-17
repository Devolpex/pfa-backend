package com.pfa.pfabackend.client;

import lombok.*;

import com.pfa.pfabackend.auth.enums.Auth;
import com.pfa.pfabackend.user.UserDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private long id;
    private Auth auth;
    private UserDTO user;

}

