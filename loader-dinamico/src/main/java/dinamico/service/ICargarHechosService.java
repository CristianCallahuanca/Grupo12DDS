package dinamico.service;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO;
import dinamico.models.entidades.hecho.HechoCrudo;

import java.util.List;

public interface ICargarHechosService {
    public void cargarHecho(HechoCrudoDTO_IN hecho);
    public List<HechoCrudoDTO> obtenerHechos();
}
