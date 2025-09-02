package dinamico.models.entidades.hecho;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class HechoCrudo{
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private String contribuyenteID;
    private List<String> archivosMultimedia;

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
    }
}