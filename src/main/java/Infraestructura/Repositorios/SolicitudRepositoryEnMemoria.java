package Infraestructura.Repositorios;

import AdministracionDeHechos.Hecho;
import SolicitudEliminar.SolicitudEliminar;

import java.util.ArrayList;
import java.util.List;

public class SolicitudRepositoryEnMemoria implements SolicitudRepository{
    private static final SolicitudRepositoryEnMemoria instance = new SolicitudRepositoryEnMemoria();
    private List<SolicitudEliminar> solicitudes = new ArrayList<>();

    private SolicitudRepositoryEnMemoria() {
    }

    public static SolicitudRepositoryEnMemoria getInstancia() {
        return instance;
    }

    @Override
    public void guardar(SolicitudEliminar solicitud) {
        solicitudes.add(solicitud);
    }

    @Override
    public SolicitudEliminar buscarPorHecho(Hecho hecho) {
        for (SolicitudEliminar s : solicitudes) {
            if (s.getHecho().equals(hecho)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public ArrayList<SolicitudEliminar> obtenerTodas() {
        return new ArrayList<>(this.solicitudes);
    }

    @Override
    public void eliminarPorHecho(Hecho hecho) {
        solicitudes.removeIf(s -> s.getHecho().equals(hecho));
    }
    @Override
    public void eliminarSolicitud(SolicitudEliminar solicitud) {
        solicitudes.remove(solicitud);
    }

}
