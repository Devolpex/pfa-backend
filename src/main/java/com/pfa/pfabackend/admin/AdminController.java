package com.pfa.pfabackend.admin;

import org.springframework.web.bind.annotation.RestController;

import com.pfa.pfabackend.admin.https.req.AdminCreateRequest;
import com.pfa.pfabackend.admin.https.req.AdminUpdateRequest;
import com.pfa.pfabackend.admin.https.res.AdminCreateResponse;
import com.pfa.pfabackend.admin.https.res.AdminDeleteResponse;
import com.pfa.pfabackend.admin.https.res.AdminPageResponse;
import com.pfa.pfabackend.admin.https.res.AdminUpdateResponse;
import com.pfa.pfabackend.user.enums.Role;
import com.pfa.pfabackend.user.models.User;
import com.pfa.pfabackend.user.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    private final UserService userService;
    private final AdminService adminService;

    // Get all admins
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AdminPageResponse> getAdminsByPagination(@RequestParam(defaultValue = "1") int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Admin> adminPage = adminService.getAdminsByPagination(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(AdminPageResponse.builder()
                .admins(adminPage.getContent())
                .currentPage(adminPage.getNumber() + 1)
                .totalPages(adminPage.getTotalPages())
                .build());
    }

    // Get admin by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        Admin admin = adminService.findAdminById(id);
        return ResponseEntity.ok(admin);
    }

    // Create admin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AdminCreateResponse> postAdmin(@RequestBody @Valid AdminCreateRequest request,
            BindingResult bindingResult) {

        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
        }
        if (userService.emailExists(request.getEmail())) {
            errors.add("Email already exists");
        }
        if (!userService.confirmPassword(request.getPassword(), request.getConfirm_password())) {
            errors.add("Password confirmation does not match");
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(AdminCreateResponse.builder().errors(errors).build());
        }
        User user = User.builder()
                .firstname(userService.firstLetterToUpperCase(request.getFirstname()))
                .lastname(userService.firstLetterToUpperCase(request.getLastname()))
                .email(request.getEmail().toLowerCase())
                .phone(request.getPhone())
                .password(userService.bcryptPassword(request.getPassword()))
                .role(Role.ADMIN)
                .created_at(new Date())
                .build();

        adminService.saveAdmin(user);

        return ResponseEntity.ok(AdminCreateResponse.builder()
                .success("Admin created successfully")
                .redirectTo("/admins")
                .build());
    }
    
    // Update admin
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AdminUpdateResponse> putAdmin(@PathVariable Long id,
            @RequestBody @Valid AdminUpdateRequest request, BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();

        Admin admin = adminService.findAdminById(id);
        if (admin == null) {
            // errors.add("Admin not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AdminUpdateResponse.builder()
                    .errors(Collections.singletonList("Admin not found")).build());
        } else {
            if (bindingResult.hasErrors()) {
                errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
            }
            if (request.getPassword() != null) {
                if (!userService.confirmPassword(request.getPassword(), request.getConfirm_password())) {
                    errors.add("Password confirmation does not match");
                } else {
                    request.setPassword(userService.bcryptPassword(request.getPassword()));
                }
            }
            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(AdminUpdateResponse.builder().errors(errors).build());
            }

            User user = admin.getUser();
            user.setFirstname(userService.firstLetterToUpperCase(request.getFirstname()));
            user.setLastname(userService.firstLetterToUpperCase(request.getLastname()));
            user.setEmail(request.getEmail().toLowerCase());
            user.setPhone(request.getPhone());
            if (request.getPassword() != null) {
                user.setPassword(request.getPassword());
            }
            admin.setUser(user);
            adminService.updateAdmin(id, admin);
        }
        return ResponseEntity.ok(AdminUpdateResponse.builder()
                .success("Admin updated successfully")
                .redirectTo("/admins")
                .build());
    }

    // Delete admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AdminDeleteResponse> deleteAdmin(@PathVariable Long id) {
        Admin admin = adminService.findAdminById(id);
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(AdminDeleteResponse.builder()
                    .errors(Collections.singletonList("Admin not found")).build());
        }
        if (adminService.deleteAdmin(id) == false) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AdminDeleteResponse.builder()
                    .errors(Collections.singletonList("Failed to delete admin")).build());
        }
        return ResponseEntity.ok(AdminDeleteResponse.builder()
                .success("Admin deleted successfully")
                .redirectTo("/admins")
                .build());
    }

}
