package dinamico.models.entidades.hecho;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HechoCrudo")
public class HechoCrudo{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    @Column(name = "fechaAcontecimiento")
    private String fechaAcontecimiento;

    @Column(name = "etiqueta")
    private String etiqueta;

    @Column(name = "contribuyenteID")
    private String contribuyenteID;

    @ElementCollection
    @CollectionTable(
            name = "hecho_multimedia",
            joinColumns = @JoinColumn(name = "hecho_id")
    )
    @Column(name = "archivoMultimedia")
    private List<String> archivosMultimedia;

    @Column(name = "lectura_agregador")
    private Boolean fueLeido;

    public HechoCrudo(String titulo, String descripcion, String categoria, String latitud, String longitud,
                      String fechaAcontecimiento,String etiqueta, String contribuyenteID,  List<String> archivosMultimedia) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.etiqueta = etiqueta;
        this.contribuyenteID = contribuyenteID;
        this.archivosMultimedia = archivosMultimedia;
        this.fueLeido = false;
    }
}