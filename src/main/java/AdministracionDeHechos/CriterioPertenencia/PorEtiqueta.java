package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Hecho;

import java.util.Objects;

public class PorEtiqueta implements CriterioDePertenencia {
    private String etiquetaDeseada;

    public PorEtiqueta(String etiquetaDeseada) {
        this.etiquetaDeseada = etiquetaDeseada;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return Objects.equals(unHecho.getEtiqueta(), etiquetaDeseada);
    }

}
