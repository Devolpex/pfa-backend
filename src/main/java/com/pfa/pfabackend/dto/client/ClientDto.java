package com.pfa.pfabackend.dto.client;

import lombok.*;


import com.pfa.pfabackend.dto.user.UserDTO;
import com.pfa.pfabackend.enums.Auth;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private long id;
    private Auth auth;
    private UserDTO user;

}

