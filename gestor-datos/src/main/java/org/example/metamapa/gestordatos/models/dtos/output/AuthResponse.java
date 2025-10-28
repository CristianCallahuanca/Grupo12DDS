package org.example.metamapa.gestordatos.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.Rol;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String message;
    private Long userId;
    private String email;
    private String nombre;
    private String apellido;
    private String rol;

    // Constructor que acepta el Enum Rol
    public AuthResponse(String token, String message, Long userId, String email,
                        String nombre, String apellido, Rol rol) {
        this.token = token;
        this.message = message;
        this.userId = userId;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol.name(); // Convertir Enum a String para JSON
    }
}
