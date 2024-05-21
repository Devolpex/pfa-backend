package com.pfa.pfabackend.demande.https.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.pfa.pfabackend.demande.DemandeDto;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandePageResponse {
    private List<DemandeDto> demandes = new ArrayList<>();
    private int currentPage;
    private int totalPages;

}
