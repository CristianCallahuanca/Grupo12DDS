package org.example.metamapa.agregador.models.entidades;

import org.example.metamapa.agregador.models.repositorios.IRepositorioHechos;
import org.example.metamapa.agregador.models.repositorios.ISpamRepository;
import org.example.metamapa.agregador.models.repositorios.implementaciones.RepositorioHechos;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class SolicitudEliminacion {
    private String id_solicitud;
    private String id_hecho;
    private String justificacion;
    private EstadoEliminar estadoEliminar;

    public SolicitudEliminacion(Hecho hecho, String justificacion) {
        this.id_solicitud = UUID.randomUUID().toString().replaceAll("-", ""); // asignación automática de ID
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

        for(Hecho hecho: RepositorioHechos.getInstance().obtenerTodosLosHechosDelSistema()){

            if(Objects.equals(hecho.getId_hecho(), id_hecho)) {
                hecho.marcarComoNoVisible();
            }
        }
    }

    public void rechazar() {
        if (this.estadoEliminar != EstadoEliminar.RECHAZADA) {
            this.estadoEliminar = EstadoEliminar.RECHAZADA;
            ISpamRepository.eliminarSolicitud(this);
        }
    }

    public void cargarSolicitud() { ISpamRepository.guardar(this); }

}
