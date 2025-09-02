package org.example.metamapa.publica.service.implementaciones;

import org.example.metamapa.publica.clientes.AdministradorClient;
import org.example.metamapa.publica.models.dtos.input.FiltroDTO;
import org.example.metamapa.publica.models.dtos.input.HechoInputDTO;
import org.example.metamapa.publica.models.dtos.input.ModoNavegacionDTO;
import org.example.metamapa.publica.models.dtos.output.HechoOutputDTO;
import org.example.metamapa.publica.service.INavegacionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavegacionService implements INavegacionService {

    private final AdministradorClient adminClient;

    public NavegacionService(AdministradorClient adminClient) {
        this.adminClient = adminClient;
    }

    @Override
    public List<HechoOutputDTO> obtenerHechosDeColeccion(String idColeccion) {
        return adminClient.obtenerHechosPorColeccion(idColeccion)
                .stream()
                .map(this::toOutput)
                .toList();
    }

    @Override
    public List<HechoOutputDTO> navegarFiltrada(FiltroDTO filtro) {
        return adminClient.navegacionFiltrada(filtro)
                .stream()
                .map(this::toOutput)
                .toList();
    }

    @Override
    public List<HechoOutputDTO> navegarModo(String idColeccion, ModoNavegacionDTO modo) {
        return adminClient.navegacionPorModo(idColeccion, modo)
                .stream()
                .map(this::toOutput)
                .toList();
    }

    private HechoOutputDTO toOutput(HechoInputDTO in) {
        HechoOutputDTO out = new HechoOutputDTO();
        out.setDescripcion(in.getDescripcion());
        out.setTipo(in.getTipo());
        out.setFuente(in.getFuente());
        out.setFecha(in.getFecha());
        out.setUbicacion(in.getUbicacion());

        return out;
    }
}

