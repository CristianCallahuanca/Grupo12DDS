package org.example.metamapa.gestordatos.Servicios;

import org.example.metamapa.gestordatos.models.entidades.CondicionDeFiltrado.CondicionDeFiltrado;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface IFiltradorService {

    public List<Hecho> filtrarHechosDataBase(List<CondicionDeFiltrado> condiciones);
}
