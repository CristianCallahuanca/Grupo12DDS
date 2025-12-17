package org.example.metamapa.Servicios;

import org.example.metamapa.models.entidades.DireccionesIP;

import java.util.List;

public interface IDireccionesIpService {

    DireccionesIP guardar(DireccionesIP direccionIP);

    void eliminarPorId(Long id);

    List<DireccionesIP> obtenerTodas();

    public boolean ipBloqueada(String ip);
}
