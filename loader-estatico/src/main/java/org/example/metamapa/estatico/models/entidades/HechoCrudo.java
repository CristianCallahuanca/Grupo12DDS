package org.example.metamapa.estatico.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hechos_crudos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HechoCrudo {


    public HechoCrudo(String titulo, String descripcion, String categoria,
                      String latitud, String longitud, String fechaAcontecimiento) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.enviado = false;
        this.fechaEnvio = null;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_hecho;

    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;

    private boolean enviado;
    private LocalDateTime fechaEnvio;

    // private String fuenteOrigen;
}
