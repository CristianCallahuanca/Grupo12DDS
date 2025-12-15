package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorCategoria;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorOrigenReal;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.PorTipoFuente;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.entidades.enums.EstadoHecho;
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
            log.debug("Filtrador sin condiciones, devolviendo todos los hechos.");
            return hechosRepository.findAll();
        }

        log.debug("Filtrador recibidas {} condiciones de filtrado:", condiciones.size());
        condiciones.forEach(c -> log.debug("   • {}", c.getClass().getSimpleName()));

        //se genera un where dinamico fuentes y categorias se tienen que hacer con OR y las demas con AND
        //ej where fecha1 AND (cat1 OR cat2) AND (fuente1 OR fuente2) AND fecha2
        //MUY IMPORTANTE EL OR SINO NUNCA MATCHEA LAS CATEGORIAS O LAS FUENTES

        // 1️⃣ OR para TipoFuente
        Specification<Hecho> tipoFuenteSpec = condiciones.stream()
                .filter(c -> c instanceof PorOrigenReal)
                .map(CondicionDeFiltrado::toSpecification)
                .reduce(Specification::or)
                .orElse(null);

        // 2️⃣ OR para Categorías
        Specification<Hecho> categoriaSpec = condiciones.stream()
                .filter(c -> c instanceof PorCategoria)
                .map(CondicionDeFiltrado::toSpecification)
                .reduce(Specification::or)
                .orElse(null);

        // 3️⃣ AND para el resto
        Specification<Hecho> otrasSpec = condiciones.stream()
                .filter(c -> !(c instanceof PorOrigenReal))
                .filter(c -> !(c instanceof PorCategoria))
                .map(CondicionDeFiltrado::toSpecification)
                .reduce(Specification::and)
                .orElse(null);

        // 4️⃣ Combinar todo
        Specification<Hecho> finalSpec = Specification.where(null);

        if (otrasSpec != null) {
            finalSpec = finalSpec.and(otrasSpec);
        }

        if (categoriaSpec != null) {
            finalSpec = finalSpec.and(categoriaSpec);
        }

        if (tipoFuenteSpec != null) {
            finalSpec = finalSpec.and(tipoFuenteSpec);
        }

        log.debug("Filtrador ejecutando consulta con Specification final...");
        List<Hecho> resultado = hechosRepository.findAll(finalSpec);

        log.debug("Filtrador resultado total: {} hechos (de {} en total).",
                resultado.size(), hechosRepository.count());

        return resultado;
    }




}
