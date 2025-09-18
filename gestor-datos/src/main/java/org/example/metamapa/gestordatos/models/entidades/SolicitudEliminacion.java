package org.example.metamapa.gestordatos.models.entidades;

import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;

public class SolicitudEliminacion {
    private String id;
    private Hecho hecho;
    private String justificacion;
    private EstadoEliminar estadoEliminar;
    private Boolean verifico_si_es_spam;
}
