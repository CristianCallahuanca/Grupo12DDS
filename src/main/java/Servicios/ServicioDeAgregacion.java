package Servicios;

import Fuentes.Fuente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServicioDeAgregacion {

    private static final ServicioDeAgregacion instance = new ServicioDeAgregacion();
    private List<Fuente> fuentes = new ArrayList<>();

    private LocalDateTime ultimaEjecucion;

    private ServicioDeAgregacion() {}

    public static ServicioDeAgregacion getInstancia() {
        return instance;
    }

    public void guardar(Fuente fuente){fuentes.add(fuente);}

}
