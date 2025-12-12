package org.example.metamapa.estatico.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fuentes_estaticas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuenteEstatica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreFuente;

    private String rutaArchivoCsv;

    private String nombreArchivoOriginal;

    private String hashUltimoProcesado;

    private Boolean pendienteProcesar;

    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaUltimoProcesamiento;

    private Boolean activa;
}

