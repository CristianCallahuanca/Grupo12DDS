package dinamico.service.implementacion;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO;
import dinamico.models.entidades.hecho.HechoCrudo;
import dinamico.models.repositorios.IRepositorioHechosCrudos;
import dinamico.service.ICargarHechosService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class cargarHechosService implements ICargarHechosService {

    //Aca debemos tener en cuenta de validar permisos (anonimo/registrado)
    //Llamar a los repositorios para persistencia
    //Preparar los DTOs

    private final IRepositorioHechosCrudos hechosRepository;

    public cargarHechosService(IRepositorioHechosCrudos hechosRepository){
        this.hechosRepository = hechosRepository;
    }

    public void cargarHecho(HechoCrudoDTO_IN hecho){

        HechoCrudo hechoCrudo = new HechoCrudo(
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getLatitud(),
                hecho.getLongitud(),
                hecho.getFechaAcontecimiento(),
                hecho.getEtiqueta(),
                hecho.getContribuyenteID(),
                hecho.getArchivosMultimedia()
        );

        hechosRepository.guardar(hechoCrudo);
    }

    public List<HechoCrudoDTO> obtenerHechos(){

        List<HechoCrudoDTO> hechos = new ArrayList<>();

        hechos = hechosRepository.obtenerHechos().stream().map(this::hechoCrudoADTO).collect(Collectors.toList());

        hechosRepository.vaciarListaHechos();

        System.out.println("se vacio la lista de hechos");

        return hechos;
    }

    public HechoCrudoDTO hechoCrudoADTO(HechoCrudo hechoCrudo){

        return new HechoCrudoDTO(hechoCrudo);
    }
}




