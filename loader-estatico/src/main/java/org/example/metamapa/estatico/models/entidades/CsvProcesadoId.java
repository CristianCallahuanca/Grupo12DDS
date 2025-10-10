package org.example.metamapa.estatico.models.entidades;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvProcesadoId implements Serializable {
    private String loaderId;
    private String nombreArchivo;
}
