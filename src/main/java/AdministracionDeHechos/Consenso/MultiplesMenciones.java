package AdministracionDeHechos.Consenso;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Servicios.ServicioDeAgregacion;

import java.util.List;

public class MultiplesMenciones extends AlgoritmoDeConsenso {
    @Override
    public List<Hecho> verificar(List<Hecho> hechos){
        List<Fuente> todasLasFuentes = ServicioDeAgregacion.getInstancia().getFuentes();

        return hechos.stream()
                .filter(h1 -> {
                    // Cantidad de fuentes que contienen un hecho igual a h1 (mismo título y mismos atributos)
                    long fuentesConMismoHecho = todasLasFuentes.stream()
                            .filter(f -> f.getHechos().stream()
                                    .anyMatch(h2 -> h2.equals(h1)))
                            .count();

                    // Alguna fuente contiene otro hecho con mismo título pero distinto (atributos diferentes)
                    boolean hayOtroDistinto = todasLasFuentes.stream()
                            .flatMap(f -> f.getHechos().stream())
                            .anyMatch(h2 -> h2.getTitulo().equals(h1.getTitulo()) && !h2.equals(h1));

                    return fuentesConMismoHecho >= 2 && !hayOtroDistinto;
                })
                .toList();
    }
}