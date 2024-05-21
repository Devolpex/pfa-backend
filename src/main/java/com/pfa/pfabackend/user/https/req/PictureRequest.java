package com.pfa.pfabackend.user.https.req;

import org.checkerframework.checker.units.qual.N;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PictureRequest {
    @NotBlank
    private String image;
}
