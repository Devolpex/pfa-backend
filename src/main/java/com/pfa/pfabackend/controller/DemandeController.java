package com.pfa.pfabackend.controller;

import com.pfa.pfabackend.dto.demande.DemandeDto;
import com.pfa.pfabackend.enums.DemandeStatus;
import com.pfa.pfabackend.request.demande.DemandeUpdateRequest;
import com.pfa.pfabackend.request.demande.DemandeUpdateStatusRequest;
import com.pfa.pfabackend.response.demande.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.pfa.pfabackend.request.demande.DemandeCreateRequest;
import com.pfa.pfabackend.service.ClientService;
import com.pfa.pfabackend.service.DemandeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandes")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DemandeController {

    private final DemandeService demandeService;
    private final ClientService clientService;

    // Create a demande
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DemandeCreateResponse> createDemande(@RequestBody @Valid DemandeCreateRequest request,
            BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(DemandeCreateResponse.builder().errors(errors).build());
        }

        try {

            if (clientService.findClientById(request.getClientId()) == null) {
                errors.add("Client does not exist");
                return ResponseEntity.badRequest().body(DemandeCreateResponse.builder().errors(errors).build());
            }

            demandeService.saveDemande(request);
            return ResponseEntity.ok(DemandeCreateResponse.builder().success("Demande created successfully").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DemandeCreateResponse.builder()
                    .errors(Collections.singletonList("Failed to create demande check try again..!")).build());
        }
    }

    // Get demande by id
    @GetMapping("/{id}")
    public ResponseEntity<GetDemandeResponse> getDemandeById(@PathVariable Long id) {
        DemandeDto demande = demandeService.findDemandeById(id);
        List<String> errors = new ArrayList<>();
        if (demande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GetDemandeResponse.builder()
                    .errors(Collections.singletonList("Demande not found")).build());
        }
        // No need for bindingResult.hasErrors() check here since we don't have
        // BindingResult
        return ResponseEntity.status(HttpStatus.OK).body(GetDemandeResponse.builder()
                .demande(demande)
                .build());
    }

    // Update a demande
    @PutMapping("/{id}")
    public ResponseEntity<DemandeUpdateResponse> updateDemande(@PathVariable Long id,
            @RequestBody @Valid DemandeUpdateRequest request, BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();

        DemandeDto demande = demandeService.findDemandeById(id);

        if (demande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DemandeUpdateResponse.builder()
                    .errors(Collections.singletonList("demande not found")).build());
        } else {

            if (demande.getClientId() != request.getClientId()) {
                // If the client is not the owner of the demande, return an error response
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(DemandeUpdateResponse.builder()
                                .errors(Collections.singletonList("Client is not the owner of the demande")).build());
            }

            // If the client is the owner, proceed with updating the demande
            if (bindingResult.hasErrors()) {
                errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
            }

            if (!errors.isEmpty()) {
                // If there are validation errors, return a bad request response
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(DemandeUpdateResponse.builder().errors(errors).build());
            }

            // Update the demande
            boolean isUpdated = demandeService.updateDemande(id, request);

            if (!isUpdated) {
                // If the demande was not updated successfully, return a not found response
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(DemandeUpdateResponse.builder().errors(Collections.singletonList("Demande not found"))
                                .build());
            }

            // If the demande was updated successfully, return a success response
            return ResponseEntity.ok(DemandeUpdateResponse.builder()
                    .success("Demande updated successfully")
                    .redirectTo("/demandes")
                    .build());
        }

    }

    // Get all demamdes
    @GetMapping
    public ResponseEntity<DemandePageResponse> getDemandesByPagination(@RequestParam(defaultValue = "1") int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DemandeDto> demandePage = demandeService.getDemandesByPagination(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(DemandePageResponse.builder()
                .demandes(demandePage.getContent())
                .currentPage(demandePage.getNumber() + 1)
                .totalPages(demandePage.getTotalPages())
                .build());
    }

    // Get demandes by client id
    @GetMapping("/client/{id}")
    public ResponseEntity<DemandePageResponse> findDemandesByClient_Id(@PathVariable Long id,
            @RequestParam(defaultValue = "1") int page) {
        int size = 5;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DemandeDto> demandePage = demandeService.findDemandesByClient_Id(id, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(DemandePageResponse.builder()
                .demandes(demandePage.getContent())
                .currentPage(demandePage.getNumber() + 1)
                .totalPages(demandePage.getTotalPages())
                .build());
    }

    // Update demande status
    @PutMapping("/{id}/status")
    public ResponseEntity<DemandeUpdateResponse> updateDemandeStatus(
            @PathVariable("id") Long id,
            @RequestBody DemandeUpdateStatusRequest request) {
        DemandeStatus newStatus = request.getStatus();

        // Call service method to update the status of the demand with the given ID
        boolean updated = demandeService.updateDemandeStatus(id, newStatus);

        if (updated) {
            return ResponseEntity.ok(DemandeUpdateResponse.builder()
                    .success("Demande updated status  successfully")
                    .redirectTo("/demandes")
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(DemandeUpdateResponse.builder().errors(Collections.singletonList("Demande not found"))
                            .build());
        }
    }

    // Delete a demande
    @DeleteMapping("/{id}")
    public ResponseEntity<DemandeDeleteResponse> deleteDemande(@PathVariable Long id) {
        DemandeDto demande = demandeService.findDemandeById(id);
        if (demande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DemandeDeleteResponse.builder()
                    .errors(Collections.singletonList("Demande not found")).build());
        }
        if (demandeService.deleteDemande(id) == false) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DemandeDeleteResponse.builder()
                    .errors(Collections.singletonList("Failed to delete demande")).build());
        }
        return ResponseEntity.ok(DemandeDeleteResponse.builder()
                .success("Demande deleted successfully")
                .redirectTo("/demandes")
                .build());
    }
}
