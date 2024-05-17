package com.pfa.pfabackend.demande.https.res;

import java.util.ArrayList;
import java.util.List;

import com.pfa.pfabackend.demande.Demande;
import com.pfa.pfabackend.demande.DemandeDto;

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
