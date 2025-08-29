package dinamico.models.entidades.hecho;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
    //private List<String> archivosMultimedia;

    //le saque la logica ya que solamente se van a persistir no es el hecho real que ese esta en el agregador
}