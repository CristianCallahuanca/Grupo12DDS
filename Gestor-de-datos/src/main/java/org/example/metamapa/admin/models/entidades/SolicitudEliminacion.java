package org.example.metamapa.admin.models.entidades;

import org.example.metamapa.admin.models.entidades.enums.EstadoEliminar;

public class SolicitudEliminacion {
    private String id;
    private Hecho hecho;
    private String justificacion;
    private EstadoEliminar estadoEliminar;
    private Boolean verifico_si_es_spam;
}
