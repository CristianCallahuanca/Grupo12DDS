package org.example.metamapa.visualizacion.models.entidades.solicitud;

public class SolicitudEliminacion {

    private Long idSolicitud;
    private Long idHecho;            // referencia lógica al hecho
    private String justificacion;
    private EstadoEliminar estado;   // PENDIENTE, APROBADA, RECHAZADA
    private boolean verificadoSpam;  // flag interno de moderación

    public SolicitudEliminacion(Long idHecho, String justificacion) {
        this.idHecho = idHecho;
        this.justificacion = justificacion;
        this.estado = EstadoEliminar.PENDIENTE;
        this.verificadoSpam = false;
    }

    public void aceptar() {
        this.estado = EstadoEliminar.APROBADA;
    }

    public void rechazar() {
        this.estado = EstadoEliminar.RECHAZADA;
    }

    public void marcarComoSpam() {
        this.verificadoSpam = true;
    }

    // Getters/Setters
    public Long getIdSolicitud() { return idSolicitud; }
    public void setIdSolicitud(Long idSolicitud) { this.idSolicitud = idSolicitud; }
    public Long getIdHecho() { return idHecho; }
    public String getJustificacion() { return justificacion; }
    public EstadoEliminar getEstado() { return estado; }
    public boolean isVerificadoSpam() { return verificadoSpam; }
}
