package com.pfa.pfabackend.client.https.res;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePictureRes {
    private String success;
    private List<String> errors;
    private String redirectTo;
    private String picture;
    private long id;

}
