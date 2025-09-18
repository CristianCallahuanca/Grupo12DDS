package org.example.metamapa.gestordatos.models.entidades;

import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;

import java.io.IOException;

public class SolicitudEliminacion {
    private String id;
    private Hecho hecho;
    private String justificacion;
    private EstadoEliminar estadoEliminar;
    private Boolean verifico_si_es_spam;

    public SolicitudEliminar(Hecho hecho, String justificacion) {
        this.id_solicitud = ServicioDeIdentificacion.getInstancia().generarIDSolicitudEliminacion(); // asignación automática de ID
        this.id_hecho = hecho.getId_hecho(); //creo
        this.justificacion = justificacion;

        if (DetectorDeSpamSingleton.getInstance().esSpam(justificacion)) {
            this.rechazar();
        } else {
            this.estadoEliminar = EstadoEliminar.PENDIENTE;
            this.cargarSolicitud();
        }
    }


    public void aceptar() throws IOException {
        this.estadoEliminar = EstadoEliminar.APROBADA;
        ServicioIdentificadorDeObjetos.getInstancia().obtenerHechoPorID(id_hecho).marcarComoNoVisible();
    }

    public void rechazar() {
        if (this.estadoEliminar != EstadoEliminar.RECHAZADA) {
            this.estadoEliminar = EstadoEliminar.RECHAZADA;
            SolicitudRepositorio.getInstancia().eliminarSolicitud(this);
        }
    }



    public void cargarSolicitud() { SolicitudRepositorio.getInstancia().guardar(this); }
}
