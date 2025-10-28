package org.example.metamapa.gestordatos.models.dtos.input;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    private String email;
    private String password;
}
