package AdministracionDeHechos;
import Persona.Contribuyente.Contribuyente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class Hecho {
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private boolean esVisible;
    private Origen origen;
    private List<String> archivosMultimedia;
    private List<String> etiquetas;
    private Contribuyente contribuyente;
}
