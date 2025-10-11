package org.example.metamapa.agregador.models.dtos.DTO_IN;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuenteDTO {
    private String nombreFuente;
    private String tipo;
    private String baseUrl;
}

