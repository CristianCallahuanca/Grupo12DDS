package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Hecho;

import java.util.Objects;

public class PorDescripcion implements CriterioDePertenencia {
    private String fraseClave;

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  unHecho.getDescripcion().toLowerCase()
                .contains(fraseClave.toLowerCase());
    }
}
