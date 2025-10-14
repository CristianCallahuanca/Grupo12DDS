package dinamico.service.implementacion;

import dinamico.models.dtos.input.HechoCrudoDTO_IN;
import dinamico.models.dtos.output.HechoCrudoDTO_OUT;
import dinamico.models.entidades.hecho.HechoCrudo;
import dinamico.models.repositorios.IRepositorioHechosCrudos;
import dinamico.service.IHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {

    private final IRepositorioHechosCrudos repositorioHechosCrudos;

    @Value("${loader.self.nombreFuente}")
    private String nombreFuente;

    public HechosService(IRepositorioHechosCrudos repositorioHechosCrudos){
        this.repositorioHechosCrudos = repositorioHechosCrudos;
    }

    public List<HechoCrudoDTO_OUT> obtenerHechos() {
        try {
            List<HechoCrudo> hechos = repositorioHechosCrudos.findByFueLeidoFalse();
            hechos.forEach(h -> h.setFueLeido(true));
            repositorioHechosCrudos.saveAll(hechos);

            List<HechoCrudoDTO_OUT> dtos = crudoDTOOuts(hechos);

            dtos.forEach(dto -> dto.setOrigen(nombreFuente));

            return dtos;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los hechos del loader dinámico", e);
        }
    }


    public void cargarHecho(HechoCrudoDTO_IN hecho){

        repositorioHechosCrudos.save(dtoInAHechoCrudo(hecho));

    }

    public void vaciarDB(){

        repositorioHechosCrudos.deleteAll();

    }

    public List<HechoCrudoDTO_OUT> crudoDTOOuts(List<HechoCrudo> hechos){

        return hechos.stream().map(e -> crudoADTOOut(e)).collect(Collectors.toList());
    }

    public HechoCrudoDTO_OUT crudoADTOOut(HechoCrudo hecho) {
        HechoCrudoDTO_OUT dto = new HechoCrudoDTO_OUT();
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setLatitud(hecho.getLatitud());
        dto.setLongitud(hecho.getLongitud());
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        dto.setEtiqueta(hecho.getEtiqueta());
        dto.setContribuyenteID("1"); //TODO HARDCODEADO TEMPORAL
        dto.setArchivosMultimedia(hecho.getArchivosMultimedia());
        dto.setTipoFuente("DINAMICA");
        return dto;
    }

    // Convierte un solo DTO_IN a HechoCrudo
    public HechoCrudo dtoInAHechoCrudo(HechoCrudoDTO_IN dto) {
        return new HechoCrudo(
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getCategoria(),
                dto.getLatitud(),
                dto.getLongitud(),
                dto.getFechaAcontecimiento(),
                dto.getEtiqueta(),
                dto.getContribuyenteID(),
                dto.getArchivosMultimedia()
        );
    }

    // Convierte una lista de DTO_IN a lista de HechoCrudo
    public List<HechoCrudo> dtoInAHechosCrudos(List<HechoCrudoDTO_IN> dtos) {
        return dtos.stream()
                .map(this::dtoInAHechoCrudo)
                .collect(Collectors.toList());
    }


}
