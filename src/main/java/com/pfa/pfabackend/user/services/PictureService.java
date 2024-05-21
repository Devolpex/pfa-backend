package com.pfa.pfabackend.user.services;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.pfa.pfabackend.basic.BasicException;
import com.pfa.pfabackend.basic.BasicResponse;
import com.pfa.pfabackend.basic.BasicValiadtion;
import com.pfa.pfabackend.file.FileService;
import com.pfa.pfabackend.file.GoogleDriveService;
import com.pfa.pfabackend.file.images.Image;
import com.pfa.pfabackend.file.images.ImageService;
import com.pfa.pfabackend.messages.Message;
import com.pfa.pfabackend.messages.enums.MessageType;
import com.pfa.pfabackend.token.JwtService;
import com.pfa.pfabackend.user.UserDTO;
import com.pfa.pfabackend.user.https.req.PictureRequest;
import com.pfa.pfabackend.user.models.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PictureService {

    private final FileService fileService;
    private final GoogleDriveService googleDriveService;
    private final BasicValiadtion basicValiadtion;
    private final UserService userService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final ImageService imageService;

    public BasicResponse uploadPicture(PictureRequest request, BindingResult bindingResult, String token)
            throws BasicException, IOException {
        Map<String, String> errors = new HashMap<>();
        // Request validation
        if (bindingResult.hasErrors()) {
            BasicResponse resposne = BasicResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(basicValiadtion.handleValidationErrors(bindingResult))
                    .build();
            throw new BasicException(resposne);
        }
        // Check if token exist
        if (token == null) {

        }
        // Extract the email from the token
        String email = jwtService.extractUsername(token);
        // Find user details by email
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        // Check if the token is valid
        if (!jwtService.isTokenValid(token, userDetails)) {
            errors.put("token", "token is not valid");
            BasicResponse response = BasicResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message(Message.builder().messages(errors)
                            .type(MessageType.ERROR).build())
                    .build();
            throw new BasicException(response);
        }
        // Check if the image is base64
        if (FileService.checkIfImage(request.getImage()) == false) {
            errors.put("image", "image is not valid try again");
            BasicResponse response = BasicResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message(Message.builder().messages(errors)
                            .type(MessageType.ERROR).build())
                    .build();
            throw new BasicException(response);

            

        }
        try {
            // Get user by email
            UserDTO userDTO = userService.getUserByEmail(email);
            // Convert to user dto to user entity
            User user = userService.buildToUserEntity(userDTO);
            // Convert the base64 string to a file
            File fileName = fileService.convertToImage(request.getImage(), userDTO.getId());
            // Get mime type
            String mimeType = fileService.getImageType(request.getImage());
            // Upload the file to Google Drive
            Map<String, String> imageInfos = googleDriveService.uploadImageToDrive(fileName, mimeType);
            // Save the image to the database
            Image image = Image.builder()
                    .url(imageInfos.get("imageUrl"))
                    .driveId(imageInfos.get("fileId"))
                    .user(user)
                    .build();
            imageService.saveImage(image);
            // Return the response
            Map<String, Object> data = new HashMap<>();
            data.put("imageInfos", image);
            return BasicResponse.builder()
                    .status(HttpStatus.OK)
                    .message(Message.builder().messages(null)
                            .type(MessageType.SUCCESS).build())
                    .data(data)
                    .build();

        } catch (Exception e) {
            errors.put("error", e.getMessage());
            // TODO: handle exception
            throw new BasicException(BasicResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message(Message.builder().messages(errors)
                            .type(MessageType.ERROR).build())
                    .build());
        }

    }

}
