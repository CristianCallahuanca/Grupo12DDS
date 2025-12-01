package org.example.metamapa.gestordatos.models.dtos.input;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GoogleAuthResponse {
    private String access_token;
    private String id_token;
    private String refresh_token;
    private Integer expires_in;
    private String token_type;
    private String scope;
}
