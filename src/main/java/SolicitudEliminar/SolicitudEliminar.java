package SolicitudEliminar;

import AdministracionDeHechos.Hecho;
import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class SolicitudEliminar {
    private Hecho hecho;
    private String justificacion;
    private EstadoEliminar estadoEliminar = EstadoEliminar.PENDIENTE;

    public SolicitudEliminar(Hecho hecho, String justificacion) {
        this.hecho = hecho;
        this.justificacion = justificacion;
    }

    public void aceptar() {
        this.estadoEliminar = EstadoEliminar.APROBADA;
        hecho.marcarComoNoVisible();
    }

    public void rechazar() {
        this.estadoEliminar = EstadoEliminar.RECHAZADA;
    }

    public void verificarSpam(DetectorDeSpam detector) {
        if (detector.esSpam(this.justificacion)) {
            this.rechazar();
        }
    }

   /* public EstadoEliminar getEstadoEliminar() {
        return estadoEliminar;
    } */
}

