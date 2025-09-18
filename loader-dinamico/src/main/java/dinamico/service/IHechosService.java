package dinamico.service;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO_OUT;

import java.util.List;

public interface IHechosService {
    public void cargarHecho(HechoCrudoDTO_IN hecho);
    public List<HechoCrudoDTO_OUT> obtenerHechos();
}
