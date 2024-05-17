package com.pfa.pfabackend.auth.https.res;



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
public class ForgetPasswordResponse {
    private String success;
    private List<String> errors = new ArrayList<>();
    private String redirectTo;

}
