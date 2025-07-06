package SolicitudEliminar;

import AdministracionDeHechos.Hecho;
import Infraestructura.Repositorios.SolicitudRepositoryEnMemoria;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class SolicitudEliminar {
    private static int contadorGlobal = 1; // contador para IDs
    private int id_solicitud; // ID único de esta solicitud

    private Hecho hecho;
    private String justificacion;
    private EstadoEliminar estadoEliminar;

    public SolicitudEliminar(Hecho hecho, String justificacion) {
        this.id_solicitud = contadorGlobal++; // asignación automática de ID
        this.hecho = hecho;
        this.justificacion = justificacion;

        if (DetectorDeSpamSingleton.getInstance().esSpam(justificacion)) {
            this.rechazar();
        } else {
            this.estadoEliminar = EstadoEliminar.PENDIENTE;
            this.cargarSolicitud();
        }
    }

    public void aceptar() {
        this.estadoEliminar = EstadoEliminar.APROBADA;
        hecho.marcarComoNoVisible();

    }

    public void rechazar() {
        if (this.estadoEliminar != EstadoEliminar.RECHAZADA) {
            this.estadoEliminar = EstadoEliminar.RECHAZADA;
            SolicitudRepositoryEnMemoria.getInstancia().eliminarSolicitud(this);
        }
    }



    public void cargarSolicitud() { SolicitudRepositoryEnMemoria.getInstancia().guardar(this); }

    /* public EstadoEliminar getEstadoEliminar() {
        return estadoEliminar;
    } */
}

