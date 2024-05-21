package com.pfa.pfabackend.file.images;

import com.pfa.pfabackend.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDTO {
    private Long id;
    private String url;
    private String driveId;
    private UserDTO userDTO;
}
