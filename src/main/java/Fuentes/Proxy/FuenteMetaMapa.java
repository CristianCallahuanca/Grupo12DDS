package Fuentes.Proxy;

import AdministracionDeHechos.Hecho;
import AdministracionDeHechos.Origen;
import SolicitudEliminar.SolicitudEliminar;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;

import java.util.HashMap;
import java.util.List;

public class FuenteMetaMapa extends FuenteProxy {
    private AdaptadorMetaMapa adapter;

    public FuenteMetaMapa(AdaptadorMetaMapa adapter) {
        this.adapter = adapter;
    }


    public void actualizarHechosDesdeAPI() {
        List<Hecho> nuevosHechos = adapter.obtenerHechosExternos(new HashMap<>());
        nuevosHechos.forEach(unHecho -> unHecho.setOrigen(Origen.PROXY));
        this.hechos.addAll(hechos); // o con logica para evitar duplicados
    }

    public void actualizarHechosDeColeccion(String identificadorColeccion) {
        List<Hecho> hechos = adapter.obtenerHechosDeColeccion(identificadorColeccion);
        this.hechos.addAll(hechos); // o con logica para evitar duplicados
    }

    public void enviarSolicitud(SolicitudEliminar solicitud) {
        adapter.enviarSolicitudDeEliminacion(solicitud);
    }


    @Override
    public void sincronizar() {
        this.actualizarHechosDesdeAPI();
    }
}

