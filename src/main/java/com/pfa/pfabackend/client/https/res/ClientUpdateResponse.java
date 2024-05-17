package com.pfa.pfabackend.client.https.res;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateResponse {
    private String success;
    private String redirectTo;
    private List<String> errors =  new ArrayList<>();

}
