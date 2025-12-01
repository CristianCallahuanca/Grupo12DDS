package org.example.metamapa.gestordatos.models.dtos.output;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.metamapa.gestordatos.models.entidades.ContribuyenteRegistrado;
import org.example.metamapa.gestordatos.models.entidades.Rol;
import org.example.metamapa.gestordatos.models.entidades.enums.Provider;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private Provider provider;   // ¡ENUM, no String!
    private Rol rol;             // Si tu Rol también es enum

    // Método estático para construir desde entidad
    public static UsuarioDTO fromEntity(ContribuyenteRegistrado entity) {
        return UsuarioDTO.builder()
                .id(entity.getUserId())
                .email(entity.getEmail())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .provider(entity.getProvider())   // Enum directo
                .rol(entity.getRol())             // Enum directo
                .build();
    }
}
