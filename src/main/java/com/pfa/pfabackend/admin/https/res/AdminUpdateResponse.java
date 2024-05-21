package com.pfa.pfabackend.admin.https.res;

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
public class AdminUpdateResponse {
    private String success;
    private List<String> errors = new ArrayList<>();
    private String redirectTo;

}
