package dinamico.service;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO_OUT;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface IHechosService {
    public void cargarHecho(HechoCrudoDTO_IN hecho,List<MultipartFile> files);
    public List<HechoCrudoDTO_OUT> obtenerHechos();
    public void vaciarDB();
}
