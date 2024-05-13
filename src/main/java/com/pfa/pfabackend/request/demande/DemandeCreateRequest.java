package com.pfa.pfabackend.request.demande;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeCreateRequest {
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Type demande cannot be empty")
    private String type;

    private Long clientId;


}

