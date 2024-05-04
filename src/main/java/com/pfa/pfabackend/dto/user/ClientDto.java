package com.pfa.pfabackend.dto.user;

import lombok.*;

import java.util.Date;

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
