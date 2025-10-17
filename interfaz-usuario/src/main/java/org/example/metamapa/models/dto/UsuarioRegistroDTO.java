package org.example.metamapa.models.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioRegistroDTO {

    private String nombre;
    private String apellido;
    private Integer dni;
    private Integer edad;
    private String username;
    private String password;
}
