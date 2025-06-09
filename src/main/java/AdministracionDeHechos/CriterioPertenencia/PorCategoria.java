package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Hecho;

import java.util.Objects;

public class PorCategoria implements CriterioDePertenencia {
    private String categoriaDeseada;

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getCategoria(), categoriaDeseada);
    }

}
