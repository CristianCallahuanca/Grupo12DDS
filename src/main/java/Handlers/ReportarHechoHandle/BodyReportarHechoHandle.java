package Handlers.ReportarHechoHandle;


import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class BodyReportarHechoHandle {

    private String titulo;
    private String descripcion;
    private String categoria;
    private String latitud;
    private String longitud;
    private String fechaAcontecimiento;
    private String etiqueta;
    private String dni;

}
