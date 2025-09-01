package org.example.metamapa.publica.service.implementaciones;

import org.example.metamapa.publica.clientes.AgregadorClient;
import org.example.metamapa.publica.models.dtos.input.HechoInputDTO;
import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.publica.service.IColeccionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColeccionService implements IColeccionService {

    private final AgregadorClient agregadorClient;

    public ColeccionService(AgregadorClient agregadorClient) {
        this.agregadorClient = agregadorClient;
    }

    @Override
    public List<HechoOutputDTO> obtenerHechosDeColeccion(String idColeccion) {
        return agregadorClient.obtenerHechosPorColeccion(idColeccion)
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

