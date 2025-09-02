package org.example.metamapa.models.entidades;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class HechoCrudo {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String ubicacion;
    private String archivosMultimedia;
    private String etiqueta;
    private String fechaAcontecimiento;
    private String fechaCarga;
    private String contribuyenteID;
    private String id_hecho;
    private String latitud;
    private String longitud;

    public List<String> getArchivosMultimediaComoLista() {
        return archivosMultimedia == null || archivosMultimedia.isBlank()
                ? List.of()
                : List.of(archivosMultimedia.split(";"));
    }

}
