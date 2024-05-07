package com.pfa.pfabackend.response.demande;

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
public class DemandeCreateResponse {
    private String success;
    private List<String> errors = new ArrayList<>();
}
