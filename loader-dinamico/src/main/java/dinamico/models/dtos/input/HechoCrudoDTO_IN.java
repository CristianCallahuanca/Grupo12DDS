package dinamico.models.dtos.input;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HechoCrudoDTO_IN {
    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private List<String> archivosMultimedia;
    private String contribuyenteID;
}
