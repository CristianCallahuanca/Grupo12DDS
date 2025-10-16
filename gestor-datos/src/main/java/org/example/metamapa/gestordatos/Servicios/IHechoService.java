package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.dtos.input.CriterioRequest;
import org.example.metamapa.gestordatos.models.dtos.input.HechoInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.Hecho;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IHechoService {

    public List<HechoOutputDTO> buscarTodosLosHechos(List<CriterioRequest> criterios);


    List<HechoOutputDTO> hechoADTOOuts(List<Hecho> hechos);

    public List<Hecho> filtrarHechos(List<CondicionDeFiltrado> condiciones);

    List<CriterioRequest> convertirQueryParamsACriterios(Map<String, String> queryParams);

    boolean editarHechoContribuyente(Long id, Map<String, Object> cambios);
    long contarTodos();
}
