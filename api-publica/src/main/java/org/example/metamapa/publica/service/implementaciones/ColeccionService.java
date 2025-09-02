package org.example.metamapa.publica.service.implementaciones;

import org.example.metamapa.publica.clientes.AdministradorClient;
import org.example.metamapa.publica.models.dtos.input.HechoInputDTO;
import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.publica.service.IColeccionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColeccionService implements IColeccionService {

    private final AdministradorClient adminClient;

    public ColeccionService(AdministradorClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    public List<HechoOutputDTO> obtenerHechosDeColeccion(String idColeccion) {
        return adminClient.obtenerHechosPorColeccion(idColeccion)
                .stream()
                .map(this::mapearAOutput)
                .toList();
    }

    private HechoOutputDTO mapearAOutput(HechoInputDTO dto) {
        HechoOutputDTO out = new HechoOutputDTO();
        out.setTitulo(dto.getTitulo());
        out.setDescripcion(dto.getDescripcion());
        out.setCategoria(dto.getCategoria());
        out.setLatitud(dto.getLatitud());
        out.setLongitud(dto.getLongitud());
        out.setFechaAcontecimiento(dto.getFechaAcontecimiento());
        out.setEtiqueta(dto.getEtiqueta());
        out.setContribuyenteID(dto.getContribuyenteID());
        out.setArchivosMultimedia(dto.getArchivosMultimedia());
        out.setSinCategorizar(dto.getSinCategorizar());
        out.setFechaAcontecimientoPosta(dto.getFechaAcontecimientoPosta());
        return out;
    }
}


