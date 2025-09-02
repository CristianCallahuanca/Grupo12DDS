package dinamico.models.dtos.output;

import dinamico.models.entidades.hecho.HechoCrudo;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Setter
@Getter
public class HechoCrudoDTO {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private String contribuyenteID;
    private List<String> archivosMultimedia;
    ////
    private String fechaCarga;

    public HechoCrudoDTO(HechoCrudo hecho) {
        this.titulo = hecho.getTitulo();
        this.descripcion = hecho.getDescripcion();
        this.categoria = hecho.getCategoria();
        this.latitud = hecho.getLatitud();
        this.longitud = hecho.getLongitud();
        this.fechaAcontecimiento = hecho.getFechaAcontecimiento();
        this.etiqueta = hecho.getEtiqueta();
        this.contribuyenteID = hecho.getContribuyenteID();
        this.archivosMultimedia = hecho.getArchivosMultimedia();
        this.fechaCarga = null;
    }
}
