package org.example.metamapa.loaderdemo.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "fuentes_demo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuenteDemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String urlBase;
    private String pathApi;
    private Integer paginaActual;
    private Boolean activa;

    private LocalDateTime ultimaConsulta;
    private String authEmail;
    private String authPassword;

    private String nombreDetectado;
    private String etiquetaDetectada;
}
