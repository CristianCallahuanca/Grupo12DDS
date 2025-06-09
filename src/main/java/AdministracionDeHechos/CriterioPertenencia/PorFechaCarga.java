package AdministracionDeHechos.CriterioPertenencia;
import AdministracionDeHechos.Hecho;

import java.time.LocalDateTime;

public class PorFechaCarga implements CriterioDePertenencia {
    private LocalDateTime desde;
    private LocalDateTime hasta;

    public PorFechaCarga(LocalDateTime fa1, LocalDateTime fc1) {
        desde = fa1;
        hasta = fc1;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaCarga().isAfter(desde) || unHecho.getFechaCarga().isEqual(desde)) &&
                (unHecho.getFechaCarga().isBefore(hasta) || unHecho.getFechaCarga().isEqual(hasta));
    }
}
