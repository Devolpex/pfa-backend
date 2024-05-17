package com.pfa.pfabackend.auth.https.res;




import com.pfa.pfabackend.basic.BasicResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse extends BasicResponse {
    private String token;
    

}
