package com.pfa.pfabackend.demande.https.req;

import com.pfa.pfabackend.demande.enums.DemandeStatus;
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
