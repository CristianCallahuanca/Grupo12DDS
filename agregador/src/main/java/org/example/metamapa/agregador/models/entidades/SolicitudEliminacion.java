package org.example.metamapa.agregador.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.agregador.models.repositorios.ISpamRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitudEliminacion")
public class SolicitudEliminacion {

    @Autowired
    private ISpamRepository repositorioSpam;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "id_hecho")
    private long id_hecho;

    @Column(name = "justificacion")
    private String justificacion;

    @Enumerated(EnumType.STRING)
    private EstadoEliminar estadoEliminar;

    public SolicitudEliminacion(Hecho hecho, String justificacion) {
        //this.id = UUID.randomUUID().toString().replaceAll("-", ""); // asignación automática de ID
        this.id_hecho = hecho.getId(); //creo
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
/*TO DO
        for(Hecho hecho: RepositorioHechos.getInstance().obtenerTodosLosHechosDelSistema()){

            if(Objects.equals(hecho.getId_hecho(), id_hecho)) {
                hecho.marcarComoNoVisible();
            }
        }*/
    }

    public void rechazar() {

    }

    public void cargarSolicitud() { repositorioSpam.save(this); }

}
