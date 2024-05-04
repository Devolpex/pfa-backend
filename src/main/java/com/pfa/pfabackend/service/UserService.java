package com.pfa.pfabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfa.pfabackend.Exception.UserNotFoundException;
import com.pfa.pfabackend.dto.user.UserCreateDto;
import com.pfa.pfabackend.dto.user.UserUpdateDto;
import com.pfa.pfabackend.model.User;
import com.pfa.pfabackend.repository.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    
    public void saveUser(UserCreateDto userCreateDto){
        // Bcrypt Password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userCreateDto.getPassword());
        // Formule the data
        String first_letter_upper_case_first_name = userCreateDto.getFirst_name().substring(0,1).toUpperCase() + userCreateDto.getFirst_name().substring(1);
        String first_letter_upper_case_last_name = userCreateDto.getLast_name().substring(0,1).toUpperCase() + userCreateDto.getLast_name().substring(1);
        String lower_case_email = userCreateDto.getEmail().toLowerCase();
        // Save the data
        User user = User.builder()
                .firstname(first_letter_upper_case_first_name)
                .lastname(first_letter_upper_case_last_name)
                .email(lower_case_email)
                .phone(userCreateDto.getPhone())
                .password(hashedPassword)
                .created_at(new Date())
                .build();
        repository.save(user);
    }

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

    public void updateUser( long id, UserUpdateDto userUpdateDto){

        //fetch the existin client from database
        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setFirstname(userUpdateDto.getFirst_name().substring(0,1).toUpperCase() + userUpdateDto.getFirst_name().substring(1));
        user.setLastname(userUpdateDto.getLast_name().substring(0,1).toUpperCase() + userUpdateDto.getLast_name().substring(1));
        user.setPhone(userUpdateDto.getPhone());
        repository.save(user);

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
}
