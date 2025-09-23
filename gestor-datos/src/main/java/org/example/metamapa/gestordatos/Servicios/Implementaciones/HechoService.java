package org.example.metamapa.gestordatos.Servicios.Implementaciones;

import org.example.metamapa.gestordatos.Servicios.IHechoService;
import org.example.metamapa.gestordatos.models.dtos.input.HechoInputDTO;
import org.example.metamapa.gestordatos.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.gestordatos.models.entidades.Hecho;
import org.example.metamapa.gestordatos.models.repositorios.IHechosRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HechoService implements IHechoService {

    private final IHechosRepository repositorioHechos;

    public HechoService(IHechosRepository repositorioHechos){
        this.repositorioHechos = repositorioHechos;
    }

    public List<HechoOutputDTO> buscarTodosLosHechos(String categoria, LocalDate fecha_reporte_desde, LocalDate fecha_reporte_hasta,
                                                     LocalDate fecha_acontecimiento_desde, LocalDate fecha_acontecimiento_hasta,
                                                     Double latitud, Double longitud){

        return hechoADTOOuts(repositorioHechos.findAll()); //TODO
    }

    public void guardarHecho(HechoInputDTO hechoInputDTO){};

    public List<HechoOutputDTO> hechoADTOOuts(List<Hecho> hechos){
        return hechos.stream().map(this::hechoADTOOut).collect(Collectors.toList());
    }

    public HechoOutputDTO hechoADTOOut(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setLatitud(String.valueOf(hecho.getUbicacion().getLatitud()));
        dto.setLongitud(String.valueOf(hecho.getUbicacion().getLongitud()));
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento().toString());
        dto.setEtiqueta(hecho.getEtiqueta());
        dto.setNombre_contribuyente(hecho.getContribuyente().getNombre());
        dto.setApellido_contribuyente(hecho.getContribuyente().getApellido());
        dto.setArchivosMultimedia(hecho.getArchivosMultimedia());
        return dto;
    }
}
