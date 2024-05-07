package com.pfa.pfabackend.controller;

import com.pfa.pfabackend.dto.demande.DemandeDto;
import com.pfa.pfabackend.model.Demande;
import com.pfa.pfabackend.response.demande.GetDemandeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.pfa.pfabackend.request.demande.DemandeCreateRequest;
import com.pfa.pfabackend.response.demande.DemandeCreateResponse;
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

    @PostMapping
//    @PreAuthorize("hasAuthority('CLIENT')")
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

            if (clientService.findClientById(request.getClient_id()) == null) {
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


    @GetMapping("/{id}")
//@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GetDemandeResponse> getDemandeById(@PathVariable Long id) {
        DemandeDto demande = demandeService.findDemandeById(id);
        List<String> errors = new ArrayList<>();
        if (demande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GetDemandeResponse.builder()
                    .errors(Collections.singletonList("Demande not found")).build());
        }
        // No need for bindingResult.hasErrors() check here since we don't have BindingResult
        return ResponseEntity.status(HttpStatus.OK).body(GetDemandeResponse.builder()
                .demande(demande)
                .build());
    }


}


