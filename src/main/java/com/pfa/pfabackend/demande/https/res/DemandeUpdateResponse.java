package com.pfa.pfabackend.demande.https.res;

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
public class DemandeUpdateResponse {
    private String success;
    private String redirectTo;
    private List<String> errors =  new ArrayList<>();
}
