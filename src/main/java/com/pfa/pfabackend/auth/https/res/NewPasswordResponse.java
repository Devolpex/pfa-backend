package com.pfa.pfabackend.auth.https.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordResponse {
    private String success;
    private List<String> errors;
    private String redirectTo;

}
