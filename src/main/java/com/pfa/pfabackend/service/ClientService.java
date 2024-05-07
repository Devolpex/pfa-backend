package com.pfa.pfabackend.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfa.pfabackend.dto.client.ClientDto;
import com.pfa.pfabackend.dto.user.UserDTO;
import com.pfa.pfabackend.enums.Auth;
import com.pfa.pfabackend.enums.Role;
import com.pfa.pfabackend.model.Client;
import com.pfa.pfabackend.model.User;
import com.pfa.pfabackend.repository.ClientRepository;
import com.pfa.pfabackend.repository.UserRepository;
import com.pfa.pfabackend.request.client.ClientCreateRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Get all clients
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    // Save client
    public void saveClient(ClientCreateRequest request) {
        // Formule the data
        request.setFirstname(
                request.getFirstname().substring(0, 1).toUpperCase() + request.getFirstname().substring(1));
        request.setLastname(request.getLastname().substring(0, 1).toUpperCase() + request.getLastname().substring(1));
        request.setEmail(request.getEmail().toLowerCase());
        // Bycrypt Password
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        // Build user data
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .role(Role.CLIENT)
                .created_at(new Date())
                .build();
        // Save user data
        userRepository.save(user);
        // Build client data
        Client client = Client.builder()
                .user(user)
                .auth(Auth.EMAIL)
                .build();
        // Save client data
        clientRepository.save(client);
    }

    // Check if the client is authenticated by email
    public boolean checkIsAuthByEmail(String email) {
        Auth authType = clientRepository.findAuthByEmail(email);
        if (authType != Auth.EMAIL) {
            return false;
        }
        return true;

    }

    // Find client by email
    public Client findClientByEmail(String email) {
        Client client = clientRepository.findByUserEmail(email);
        return client;
    }



    // Convert client to DTO
    private ClientDto convertToDTO(Client client) {
        UserDTO user = UserDTO.builder()
                .id(client.getUser().getId())
                .lastname(client.getUser().getLastname())
                .firstname(client.getUser().getFirstname())
                .email(client.getUser().getEmail())
                .phone(client.getUser().getPhone())
                .image(client.getUser().getImage())
                .created_at(client.getUser().getCreated_at())
                .build();
        ClientDto clientDto = ClientDto.builder()
                .id(client.getId())
                .auth(client.getAuth())
                .user(user)
                .build();
        return clientDto;
    }

    // Find client by id
    public Client findClientById(long id) {
        Client query = clientRepository.findById(id).orElse(null);
        if (query == null) {
            return null;
        }
        User user = User.builder()
                .id(query.getUser().getId())
                .firstname(query.getUser().getFirstname())
                .lastname(query.getUser().getLastname())
                .email(query.getUser().getEmail())
                .phone(query.getUser().getPhone())
                .role(query.getUser().getRole())
                .created_at(query.getUser().getCreated_at())
                .build();
        Client client = Client.builder()
                .id(query.getId())
                .auth(query.getAuth())
                .user(user)
                .build();
        return client;
    }

    // Delete client
    public boolean deleteClient(long id) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client != null) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<ClientDto> getClientsByPagination(Pageable pageable) {
        Page<Client> clients = clientRepository.findAll(pageable);
        return clients.map(this::convertToDTO);
    }

    // search client by email first or last name
    public Page<ClientDto> searchClients(String search, Pageable pageable) {
        Page<Client> clients = clientRepository.findByEmailOrFirstNameOrLastName(search, pageable);
        return clients.map(this::convertToDTO);
    }

    // Update client
    public void updateClient(Long id, Client client) {
        User user = client.getUser();
        user.setUpdate_at(new Date());
        userRepository.save(user);
    }

}
