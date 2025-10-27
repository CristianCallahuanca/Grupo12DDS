package org.example.metamapa.agregador.service.implementacion;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.metamapa.agregador.models.entidades.Categoria;
import org.example.metamapa.agregador.models.entidades.Sinonimo;
import org.example.metamapa.agregador.models.repositorios.ICategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CatalogoCategoriasService {

    @Autowired
    private ICategoriaRepository categoriaRepo;

    private Map<String, Categoria> categoriasPorNombre;
    private Map<String, List<String>> sinonimosPorCategoria;

    @PostConstruct
    public void inicializarCatalogo() {
        List<Categoria> categorias = categoriaRepo.findAll();
        if (categorias.isEmpty()) {
            log.warn("No se encontraron categorías en la base de datos al iniciar el servicio.");
        }

        categoriasPorNombre = categorias.stream()
                .collect(Collectors.toMap(Categoria::getNombre, c -> c));

        sinonimosPorCategoria = categorias.stream()
                .collect(Collectors.toMap(
                        Categoria::getNombre,
                        c -> c.getSinonimos().stream()
                                .map(Sinonimo::getPalabra)
                                .toList()
                ));

        log.info("Catálogo cargado en memoria con {} categorías y {} sinónimos totales.",
                categorias.size(),
                sinonimosPorCategoria.values().stream().mapToInt(List::size).sum());
    }

    public Map<String, List<String>> obtenerSinonimosPorCategoria() {
        return sinonimosPorCategoria;
    }

    public Categoria obtenerCategoriaPorNombre(String nombre) {
        return categoriasPorNombre.get(nombre);
    }
}


/*public void recargarCatalogo() {
    inicializarCatalogo();
}
POST /gestordatos/admin/recargar-catalogo
*/
