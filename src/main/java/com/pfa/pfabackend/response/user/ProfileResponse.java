package com.pfa.pfabackend.response.user;

import java.util.ArrayList;
import java.util.List;

import com.pfa.pfabackend.dto.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private UserDTO user;
    private List<String> errors = new ArrayList<>();
    private String success;
}
