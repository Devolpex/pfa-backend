package com.pfa.pfabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfa.pfabackend.Exception.NotFoundException;
import com.pfa.pfabackend.Exception.UserNotFoundException;
import com.pfa.pfabackend.dto.user.UserDTO;
import com.pfa.pfabackend.enums.Role;
import com.pfa.pfabackend.model.Client;
import com.pfa.pfabackend.model.User;
import com.pfa.pfabackend.repository.AdminRepository;
import com.pfa.pfabackend.repository.ClientRepository;
import com.pfa.pfabackend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;

    

    public List<User> findAllUsers(){
        return repository.findAll();
    }

    public boolean emailExists(String email){
        return repository.existsByEmail(email);
    }
    public boolean confirmPassword(String password,String confirm){
        return password.equals(confirm);
    }
    public void deleteUserById(long id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public String bcryptPassword(String password){
        return passwordEncoder.encode(password);
    }

    public String firstLetterToUpperCase(String word){
        return word.substring(0,1).toUpperCase() + word.substring(1);
    }

    public boolean PasswordIsEmpty(String password, String confirmpassword) {
        if (password == null || confirmpassword == null || password.isEmpty() || confirmpassword.isEmpty()) {
            return true;
        } else return false;
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
        return this.convertToUserDTO(user) ;

    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .lastname(user.getLastname())
                .firstname(user.getFirstname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .image(user.getImage())
                .created_at(user.getCreated_at())
                .role(user.getRole())
                .clientId(user.getClient().getId())
                .build();
    }

    
}
