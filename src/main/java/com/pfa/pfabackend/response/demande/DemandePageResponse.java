package com.pfa.pfabackend.response.demande;

import com.pfa.pfabackend.dto.demande.DemandeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandePageResponse {
    private List<DemandeDto> demandes = new ArrayList<>();
    private int currentPage;
    private int totalPages;

}
