package com.pfa.pfabackend.user.services;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfa.pfabackend.admin.AdminRepository;
import com.pfa.pfabackend.auth.services.PasswordService;
import com.pfa.pfabackend.basic.BasicException;
import com.pfa.pfabackend.basic.BasicResponse;
import com.pfa.pfabackend.client.Client;
import com.pfa.pfabackend.client.ClientRepository;
import com.pfa.pfabackend.exception.NotFoundException;
import com.pfa.pfabackend.exception.NotValidException;
import com.pfa.pfabackend.messages.Message;
import com.pfa.pfabackend.messages.enums.MessageType;
import com.pfa.pfabackend.token.JwtService;
import com.pfa.pfabackend.user.UserDTO;
import com.pfa.pfabackend.user.UserNotFoundException;
import com.pfa.pfabackend.user.data.UserRepository;
import com.pfa.pfabackend.user.enums.Role;
import com.pfa.pfabackend.user.https.req.UpdatePasswordRequest;
import com.pfa.pfabackend.user.https.req.UpdateUserInfosRequest;
import com.pfa.pfabackend.user.models.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordService passwordService;

    // Change user password api service
    public Message changePassword(UpdatePasswordRequest request) throws
    BasicException {
    // Exctract user email from token
    String userEmail = jwtService.extractUsername(request.getToken());
    // Find userDetail by email
    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    // Validate token
    if (!jwtService.isTokenValid(request.getToken(), userDetails)) {
    throw new BasicException("Token is not valid", HttpStatus.UNAUTHORIZED);
    }
    // Check old password
    if (!passwordService.checkPassword(request.getOldPassword(),
    userDetails.getPassword())) {
    throw new BasicException("Old password is not valid",
    HttpStatus.BAD_REQUEST);
    }
    // Confirm new password
    if (!confirmPassword(request.getNewPassword(),
    request.getConfirmNewPassword())) {
    throw new BasicException("New password and confirm password are not the
    same", HttpStatus.BAD_REQUEST);
    }
    // Get User by email
    UserDTO userDTO = this.getUserByEmail(userEmail);
    // Bcrypt new password
    userDTO.setPassword(passwordService.bcryptPassword(request.getNewPassword()));
    // Update user password
    this.saveUser(userDTO);
    return Message.builder().message("Password updated
    successfully").type(MessageType.SUCCESS).build();
    }

    // Update user informatio api service
    public Message updateUserInfos(UpdateUserInfosRequest request) throws
    BasicException {
    // Exctract user email from token
    String userEmail = jwtService.extractUsername(request.getToken());
    // Find userDetail by email
    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    // Validate token
    if (!jwtService.isTokenValid(request.getToken(), userDetails)) {
    throw new BasicException("Token is not valid", HttpStatus.UNAUTHORIZED);
    }
    // Get user by email
    UserDTO userDTO = this.getUserByEmail(userEmail);
    // Update user informations
    this.updateUser(userDTO, request);
    // Return message

    return Message.builder().message("User informations updated successfully").type(MessageType.SUCCESS).build();

    }

    // Find user by email and build to DTO
    public UserDTO getUserByEmail(String email) {
        User user = repository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new NotFoundException("User not found with email :  " + email);
        }
        return buildToUserDTO(user);
    }

    // Check if email exists excluding the current user's email
    private boolean emailExistsExcludingCurrentUser(String email, String currentUserEmail) {
        User user = repository.findByEmail(email).orElse(null);
        return user != null && !user.getEmail().equals(currentUserEmail);
    }

    // Save user
    public void saveUser(UserDTO userDTO) {
        LocalDate currentDate = LocalDate.now();
        // Convert LocalDate to java.sql.Date
        Date sqlDate = Date.valueOf(currentDate);
        // Set the created_at field to the SQL Date
        userDTO.setCreated_at(sqlDate);
        // Build the User entity and save it
        User user = buildToUserEntity(userDTO);
        repository.save(user);
    }

    // Update user
    public void updateUser(UserDTO userDTO, UpdateUserInfosRequest request) {
        // Build user entity
        User user = buildToUserEntity(userDTO);
        // Update user information from request
        userDTO.setUsername(request.getUsername());
        userDTO.setLastname(request.getLastname());
        userDTO.setFirstname(request.getFirstname());
        userDTO.setEmail(request.getEmail());
        userDTO.setPhone(request.getPhone());
        // Save user
        repository.save(user);
    }

    //

    // Build Entity
    public User buildToUserEntity(UserDTO userDTO) {
        User user = User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .lastname(userDTO.getLastname())
                .firstname(userDTO.getFirstname())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .image(userDTO.getImage())
                .role(userDTO.getRole())
                .password(userDTO.getPassword())
                .build();
        return user;
    }

    // Build to DTO
    public UserDTO buildToUserDTO(User user) {
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .image(user.getImage())
                .role(user.getRole())
                .password(user.getPassword())
                .build();
        return userDTO;
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return repository.existsByEmail(email);
    }

    // Check if the username exists
    public boolean usernameExists(String username) {
        return repository.existsByUsername(username);
    }

    public boolean confirmPassword(String password, String confirm) {
        return password.equals(confirm);
    }

    public void deleteUserById(long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public String bcryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String firstLetterToUpperCase(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public boolean PasswordIsEmpty(String password, String confirmpassword) {
        if (password == null || confirmpassword == null || password.isEmpty() || confirmpassword.isEmpty()) {
            return true;
        } else
            return false;
    }

    public String findPasswordByEmail(String email) {
        User user = repository.findByEmail(email).orElse(null);
        if (user != null) {
            return user.getPassword();
        }
        return null;
    }

    public UserDTO findUserByEmail(String email) {
        User user = repository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new NotFoundException("User not found with email :  " + email);
        }

        Role role = user.getRole();
        Long clientId = null;
        Long adminId = null;
        if (role.equals(Role.CLIENT)) {
            clientId = user.getClient().getId();
        } else if (role.equals(Role.ADMIN)) {
            adminId = user.getAdmin().getId();
        }

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .image(user.getImage())
                .created_at(user.getCreated_at())
                .clientId(clientId)
                .adminId(adminId)
                .role(user.getRole())
                .build();
        return userDTO;

    }

}
