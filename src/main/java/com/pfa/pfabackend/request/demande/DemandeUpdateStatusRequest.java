package com.pfa.pfabackend.request.demande;

import com.pfa.pfabackend.enums.DemandeStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeUpdateStatusRequest {
    @NotBlank(message = "Description cannot be empty")
    private DemandeStatus status;
}
