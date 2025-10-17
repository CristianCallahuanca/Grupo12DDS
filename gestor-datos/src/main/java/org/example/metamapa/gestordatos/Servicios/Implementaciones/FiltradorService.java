package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorOrigen;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FiltradorService {

    private final IHechosRepository hechosRepository;

    public FiltradorService(IHechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    public List<Hecho> filtrarHechosDataBase(List<CondicionDeFiltrado> condiciones) {


        if (condiciones == null || condiciones.isEmpty()) {
            log.debug("Filtrador No se recibieron condiciones. Devolviendo todos los hechos.");
            return hechosRepository.findAll();
        }

        log.debug("Filtrador Recibidas {} condiciones de filtrado:", condiciones.size());
        condiciones.forEach(c -> log.debug("   • {}", c.getClass().getSimpleName()));

        long countOrigen = condiciones.stream().filter(c -> c instanceof PorOrigen).count();
        long countOtros  = condiciones.stream().filter(c -> !(c instanceof PorOrigen)).count();
        log.debug("Filtrador {} condición(es) de tipo PorOrigen y {} de otro tipo", countOrigen, countOtros);


        Specification<Hecho> origenSpec = condiciones.stream()
                .filter(c -> c instanceof PorOrigen)
                .map(c -> {
                    log.debug("Generando Specification de PorOrigen: {}", c);
                    return c.toSpecification();
                })
                .reduce(Specification::or)
                .orElse(null);

        Specification<Hecho> otrasSpec = condiciones.stream()
                .filter(c -> !(c instanceof PorOrigen))
                .map(c -> {
                    log.debug("Generando Specification de {}", c.getClass().getSimpleName());
                    return c.toSpecification();
                })
                .reduce(Specification::and)
                .orElse(null);

        Specification<Hecho> finalSpec =
                (origenSpec != null && otrasSpec != null) ? otrasSpec.and(origenSpec)
                        : (otrasSpec != null) ? otrasSpec : origenSpec;

        log.debug("Filtrador Specification final construida. Ejecutando consulta...");

        List<Hecho> resultado = hechosRepository.findAll(finalSpec);
        log.debug("Filtrador Resultado total del filtrado: {} hechos.", resultado.size());

        long totalHechos = hechosRepository.count();
        log.debug("Filtrador Hechos totales en BD (sin filtro): {}", totalHechos);

        return resultado;
    }


    public List<Hecho> filtrarHechos(List<Hecho> unosHechos, List<CondicionDeFiltrado> filtros) {
        if (unosHechos == null || unosHechos.isEmpty()) return List.of();
        if (filtros == null || filtros.isEmpty()) return unosHechos;

        return unosHechos.stream()
                .filter(h -> cumpleFiltrosAgrupados(h, filtros))
                .toList();
    }

    private static boolean cumpleFiltrosAgrupados(Hecho hecho, List<CondicionDeFiltrado> filtros) {
        Map<Class<? extends CondicionDeFiltrado>, List<CondicionDeFiltrado>> porTipo =
                filtros.stream().collect(Collectors.groupingBy(CondicionDeFiltrado::getClass));

        return porTipo.values().stream()
                .allMatch(grupo -> grupo.stream().anyMatch(f -> f.cumpleUno(hecho)));
    }


    private static Boolean coincidenTipos(CondicionDeFiltrado unFiltro, CondicionDeFiltrado otroFiltro) {
        return unFiltro.getClass() == otroFiltro.getClass();
    }

}
