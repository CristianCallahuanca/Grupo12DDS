package dinamico.service;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO_OUT;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IHechosService {
    public void cargarHecho(HechoCrudoDTO_IN hecho);
    public List<HechoCrudoDTO_OUT> obtenerHechos();
}
