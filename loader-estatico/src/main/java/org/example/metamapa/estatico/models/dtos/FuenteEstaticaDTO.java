package org.example.metamapa.estatico.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteEstaticaDTO {

    private Long id;

    private String nombreFuente;

    private String nombreArchivoOriginal;

    private Boolean pendienteProcesar;

    private Boolean activa;

    private LocalDateTime fechaUltimoProcesamiento;
}


