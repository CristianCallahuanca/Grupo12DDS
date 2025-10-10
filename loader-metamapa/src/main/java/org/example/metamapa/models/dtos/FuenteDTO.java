package org.example.metamapa.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteDTO {
    private String nombreFuente;
    private String tipo;
    private String baseUrl;
}
