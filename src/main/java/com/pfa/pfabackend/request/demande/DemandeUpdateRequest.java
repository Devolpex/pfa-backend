package com.pfa.pfabackend.request.demande;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeUpdateRequest {
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Type demande cannot be empty")
    private String type;

    @NotNull(message = "Client id empty")
    private Long clientId;
}
