package org.example.metamapa.estatico.models.entidades;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "csv_procesados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvProcesado {

    @EmbeddedId
    private CsvProcesadoId id;

    private String hash;
    private LocalDateTime fechaProcesamiento;
}
