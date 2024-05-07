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
    private String status;
    @NotBlank(message = "Type demande cannot be empty")
    private String type_demande;
    @NotBlank(message = "Date cannot be empty")
    private String date_demande;
    @NotBlank(message = "Client cannot be empty")
    private int client_id;


}

