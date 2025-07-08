package SolicitudEliminar;

import AdministracionDeHechos.Hecho;
import Infraestructura.Repositorios.SolicitudRepositoryEnMemoria;
import Servicios.ServicioDeIdentificacion;
import Servicios.ServicioIdentificadorDeObjetos;
import lombok.Setter;
import lombok.Getter;

import java.io.IOException;

@Setter
@Getter
public class SolicitudEliminar {
    private int id_solicitud;
    private int id_hecho;
    private String justificacion;
    private EstadoEliminar estadoEliminar;

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
            SolicitudRepositoryEnMemoria.getInstancia().eliminarSolicitud(this);
        }
    }



    public void cargarSolicitud() { SolicitudRepositoryEnMemoria.getInstancia().guardar(this); }

    /* public EstadoEliminar getEstadoEliminar() {
        return estadoEliminar;
    } */
}

