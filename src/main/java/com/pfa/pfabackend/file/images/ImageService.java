package com.pfa.pfabackend.file.images;

import org.springframework.stereotype.Service;

import com.pfa.pfabackend.user.models.User;
import com.pfa.pfabackend.user.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

    private ImageRepo imageRepo;
    private UserService userService;

    // Method to save image to database
    public Image saveImage(Image image) {
        return imageRepo.save(image);
    }

    // Build Entity from DTO
    public Image buildImageEntity(ImageDTO imageDTO) {
        return Image.builder()
                .id(imageDTO.getId())
                .url(imageDTO.getUrl())
                .driveId(imageDTO.getDriveId())
                .user(userService.buildToUserEntity(imageDTO.getUserDTO()))
                .build();
    }

    // Build DTO from Entity
    public ImageDTO buildImageDTO(Image image) {
        return ImageDTO.builder()
                .id(image.getId())
                .url(image.getUrl())
                .driveId(image.getDriveId())
                .userDTO(userService.buildToUserDTO(image.getUser()))
                .build();
    }
    
}
