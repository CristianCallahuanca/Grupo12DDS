package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.input.HechoInputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HechoService implements IHechoService {

    public List<Hecho> buscarTodos();
    public void guardarHecho(HechoInputDTO hechoInputDTO);
}
