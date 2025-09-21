package dinamico.models.dtos.output;

import dinamico.models.entidades.hecho.HechoCrudo;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Setter
@Getter
public class HechoCrudoDTO_OUT {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private String contribuyenteID;
    private List<String> archivosMultimedia;

}
