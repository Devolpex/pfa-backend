package com.pfa.pfabackend.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfa.pfabackend.admin.AdminRepository;
import com.pfa.pfabackend.client.Client;
import com.pfa.pfabackend.client.ClientRepository;
import com.pfa.pfabackend.exception.NotFoundException;
import com.pfa.pfabackend.user.UserDTO;
import com.pfa.pfabackend.user.UserNotFoundException;
import com.pfa.pfabackend.user.data.UserRepository;
import com.pfa.pfabackend.user.enums.Role;
import com.pfa.pfabackend.user.models.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public boolean emailExists(String email) {
        return repository.existsByEmail(email);
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
