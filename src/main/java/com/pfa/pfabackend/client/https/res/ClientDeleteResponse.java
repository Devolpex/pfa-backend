package com.pfa.pfabackend.client.https.res;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDeleteResponse {
    private String success;
    private List<String> errors = new ArrayList<>();
    private String redirectTo;
    
}
