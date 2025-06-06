package AdministracionDeHechos.CriterioPertenencia;
import AdministracionDeHechos.Hecho;

import java.time.LocalDateTime;

public class PorFechaCarga implements CriterioDePertenencia {
    private LocalDateTime desde;
    private LocalDateTime hasta;
    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaCarga().isAfter(desde) || unHecho.getFechaCarga().isEqual(desde)) &&
                (unHecho.getFechaCarga().isBefore(hasta) || unHecho.getFechaCarga().isEqual(hasta));
    }
}
