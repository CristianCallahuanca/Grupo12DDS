package org.example.metamapa.gestordatos.models.Consenso;

import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.HechoDeColeccion;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MultiplesMenciones extends AlgoritmoConsenso {

    @Override
    public void consensuarHechos(List<HechoDeColeccion> hechosDeColeccion) {
        var grupos = hechosDeColeccion.stream()
                .collect(Collectors.groupingBy(hdc -> hdc.getHecho().getTitulo()));

        grupos.forEach((titulo, grupo) -> {
            boolean variasFuentes = grupo.stream()
                    .map(hdc -> hdc.getHecho().getOrigenes())
                    .flatMap(List::stream)
                    .distinct()
                    .count() > 1;

            boolean hayConflictos = grupo.stream().anyMatch(hdc1 ->
                    grupo.stream().anyMatch(hdc2 ->
                            !Objects.equals(hdc1, hdc2) &&
                                    !atributosCoinciden(hdc1.getHecho(), hdc2.getHecho())
                    )
            );

            boolean consensuado = variasFuentes && !hayConflictos;
            grupo.forEach(hdc -> hdc.setConsensuado(consensuado));
        });
    }

    private boolean atributosCoinciden(Hecho a, Hecho b) {
        return Objects.equals(a.getDescripcion(), b.getDescripcion()) &&
                Objects.equals(a.getCategoria(), b.getCategoria()) &&
                Objects.equals(a.getFechaAcontecimiento(), b.getFechaAcontecimiento());
    }

    @Override
    public boolean esConsensuado(Hecho hecho) {
        // No se usa directamente en este algoritmo.
        return false;
    }
}


