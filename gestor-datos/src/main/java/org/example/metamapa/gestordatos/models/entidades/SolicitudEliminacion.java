package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitud_eliminacion")
public class SolicitudEliminacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToOne
    @JoinColumn(name = "hecho_id")
    private Hecho hecho;

    @Column(name = "justificacion")
    private String justificacion;

    @Enumerated(EnumType.STRING)
    private EstadoEliminar estadoEliminar;

    @Column(name = "verifico_si_es_spam")
    private Boolean verifico_si_es_spam;


    /*
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



    public void cargarSolicitud() { SolicitudRepositorio.getInstancia().guardar(this); }*/
}
