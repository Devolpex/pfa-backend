package com.pfa.pfabackend.controller;

import com.pfa.pfabackend.model.Demande;
import com.pfa.pfabackend.response.demande.GetDemandeResponse;
import com.pfa.pfabackend.service.DemandeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandes")
@RequiredArgsConstructor
public class DemandeController {
    private final DemandeService demandeService;
    @GetMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GetDemandeResponse> getDemandeById(@PathVariable Long id, BindingResult bindingResult) {
        Demande demande = demandeService.findDemandeById(id);
        List<String> errors = new ArrayList<>();
        if (demande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GetDemandeResponse.builder()
                    .errors(Collections.singletonList("Demande not found")).build());
        }
        if (bindingResult.hasErrors()) {
            errors = bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(GetDemandeResponse.builder().errors(errors).build());
        }
        return ResponseEntity.status(HttpStatus.OK).body(GetDemandeResponse.builder()
                .demande(demande)
                .build());
    }
}
