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

    private HechoOutputDTO mapearAOutput(HechoInputDTO input) {
        HechoOutputDTO output = new HechoOutputDTO();

        output.setDescripcion(input.getDescripcion());
        output.setTipo(input.getTipo());
        output.setFuente(input.getFuente());
        output.setFecha(input.getFecha());
        output.setUbicacion(input.getUbicacion());
        return output;
    }
}


