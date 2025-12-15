package org.example.metamapa.gestordatos.controllers.publica;


import lombok.RequiredArgsConstructor;
import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.conversores.StringAObjetos;
import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.input.HechoFiltroInput;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HechoGraphQLController {

    private final IHechoService hechosService;

    public HechoGraphQLController(IHechoService hechosService) {
        this.hechosService = hechosService;
    }

    @QueryMapping
    public List<HechoOutputDTO> obtenerHechosFiltrados(
            @Argument HechoFiltroInput filtro
    ) {

        // 1️⃣ Convertimos el input GraphQL en el MISMO Map que usa el controller REST
        Map<String, String> queryParams = new HashMap<>();

        if (filtro != null) {
            agregar(queryParams, "sur", filtro.getSur());
            agregar(queryParams, "oeste", filtro.getOeste());
            agregar(queryParams, "norte", filtro.getNorte());
            agregar(queryParams, "este", filtro.getEste());

            agregar(queryParams, "titulo", filtro.getTitulo());
            agregar(queryParams, "descripcion", filtro.getDescripcion());
            agregar(queryParams, "categoria", filtro.getCategoria());

            agregar(queryParams, "contieneMultimedia", filtro.getContieneMultimedia());

            agregar(queryParams, "desdeAcontecimiento", filtro.getDesdeAcontecimiento());
            agregar(queryParams, "hastaAcontecimiento", filtro.getHastaAcontecimiento());
            agregar(queryParams, "desdeCarga", filtro.getDesdeCarga());
            agregar(queryParams, "hastaCarga", filtro.getHastaCarga());

            agregar(queryParams, "estadoDeseado", filtro.getEstadoDeseado());
            agregar(queryParams, "coleccionId", filtro.getColeccionId());
            agregar(queryParams, "modo", filtro.getModo());
        }

        // 2️⃣ Reutilizamos EXACTAMENTE tu lógica existente
        List<CriterioRequest> criterios =
                StringAObjetos.convertirQueryParamsACriterios(queryParams);

        return hechosService.buscarTodosLosHechos(criterios);
    }

    private void agregar(Map<String, String> map, String key, Object value) {
        if (value != null) {
            map.put(key, value.toString());
        }
    }
}
