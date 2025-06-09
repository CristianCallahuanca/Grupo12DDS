package AdministracionDeHechos.CriterioPertenencia;

import AdministracionDeHechos.Hecho;

import java.util.Objects;

public class PorTitulo implements CriterioDePertenencia {
    private String tituloBuscado;

    public PorTitulo(String titulo) {
        this.tituloBuscado = titulo;
    }

    @Override
    public boolean cumpleUno(Hecho unHecho) {
        return  Objects.equals(unHecho.getTitulo(), tituloBuscado);
    }

}
