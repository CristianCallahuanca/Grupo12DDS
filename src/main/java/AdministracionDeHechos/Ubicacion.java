package AdministracionDeHechos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ubicacion {
    private double latitud;
    private double longitud;

    public Ubicacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
