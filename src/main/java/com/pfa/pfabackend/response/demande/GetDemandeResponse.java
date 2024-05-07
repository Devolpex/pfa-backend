package com.pfa.pfabackend.response.demande;

import java.util.ArrayList;
import java.util.List;

import com.pfa.pfabackend.dto.demande.DemandeDto;
import com.pfa.pfabackend.model.Demande;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetDemandeResponse {
    private DemandeDto demande;
    private List<String> errors =  new ArrayList<>();
}
