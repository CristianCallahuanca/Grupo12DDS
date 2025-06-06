package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Hecho;

import java.time.LocalDateTime;

public class PorFechaAcontecimiento implements CriterioDePertenencia {
    private LocalDateTime desde;
    private LocalDateTime hasta;
    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return (unHecho.getFechaAcontecimiento().isAfter(desde) || unHecho.getFechaAcontecimiento().isEqual(desde)) &&
                (unHecho.getFechaAcontecimiento().isBefore(hasta) || unHecho.getFechaAcontecimiento().isEqual(hasta));
    }
}
