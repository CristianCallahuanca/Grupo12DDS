package org.example.metamapa.estatico.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteEstaticaDTO {
    private Long id;
    private String nombreFuente;
    private String origen;      // "CSV_SUBIDO" / "CSV_URL"
    private String urlCsv;      // opcional, si corresponde
    private String nombreArchivoOriginal;  // opcional
}
