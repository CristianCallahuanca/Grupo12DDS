package org.example.metamapa.gestordatos.models.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoEliminar;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitud_eliminacion")
public class SolicitudEliminacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false) // o @OneToOne si busco que sea exclusivo
    @JoinColumn(name = "hecho_id", nullable = false)
    private Hecho hecho;

    @Column(name = "justificacion", nullable = false, length = 500)
    private String justificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_eliminar", nullable = false)
    private EstadoEliminar estadoEliminar;

    @Column(name = "verifico_si_es_spam", nullable = false)
    private boolean verificoSiEsSpam;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();


    public SolicitudEliminacion(Hecho hecho, String justificacion) {
        this.hecho = hecho;
        this.justificacion = justificacion;
        this.estadoEliminar = EstadoEliminar.PENDIENTE;
        this.verificoSiEsSpam = false;
    }
}
