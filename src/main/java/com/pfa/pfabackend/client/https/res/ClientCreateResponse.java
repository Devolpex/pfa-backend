package com.pfa.pfabackend.client.https.res;

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
public class ClientCreateResponse {
    private String success;
    private List<String> errors = new ArrayList<>();
}
