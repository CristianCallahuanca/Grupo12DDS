package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorTipoFuente;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FiltradorService {

    private final IHechosRepository hechosRepository;

    public FiltradorService(IHechosRepository hechosRepository) {
        this.hechosRepository = hechosRepository;
    }

    public List<Hecho> filtrarHechosDataBase(List<CondicionDeFiltrado> condiciones) {

        if (condiciones == null || condiciones.isEmpty()) {
            log.debug("Filtrador Sin condiciones, devolviendo todos los hechos.");
            return hechosRepository.findAll();
        }

        log.debug("Filtrador Recibidas {} condiciones de filtrado:", condiciones.size());
        condiciones.forEach(c -> log.debug("   • {}", c.getClass().getSimpleName()));

        Specification<Hecho> tipoFuenteSpec = condiciones.stream()
                .filter(c -> c instanceof PorTipoFuente)
                .map(CondicionDeFiltrado::toSpecification)
                .reduce(Specification::or)
                .orElse(null);

        Specification<Hecho> otrasSpec = condiciones.stream()
                .filter(c -> !(c instanceof PorTipoFuente))
                .map(CondicionDeFiltrado::toSpecification)
                .reduce(Specification::and)
                .orElse(null);

        Specification<Hecho> finalSpec = (tipoFuenteSpec != null && otrasSpec != null)
                ? otrasSpec.and(tipoFuenteSpec)
                : (otrasSpec != null ? otrasSpec : tipoFuenteSpec);

        log.debug("Filtrador Ejecutando consulta con Specification final...");
        List<Hecho> resultado = hechosRepository.findAll(finalSpec);
        log.debug("Filtrador Resultado total: {} hechos (de {} en total).",
                resultado.size(), hechosRepository.count());

        return resultado;
    }



}
