package org.example.metamapa.gestordatos.models.entidades;


import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Es necesario para los algoritmos de concenso
public class Hecho {
    // acordarme de cambiar origen a orígenes
    private long id;

    private String titulo;

    private String descripcion;

    private String categoria;

    private Ubicacion ubicacion;

    private LocalDateTime fechaAcontecimiento;

    private LocalDateTime fechaCarga;

    private EstadoHecho estadoHecho;

    private EstadoEdicionHecho estadoEdicionHecho;

    private List<String> archivosMultimedia;

    private String etiqueta;


    private ContribuyenteRegistrado contribuyente;

    private List<Origen> origenes;



}
