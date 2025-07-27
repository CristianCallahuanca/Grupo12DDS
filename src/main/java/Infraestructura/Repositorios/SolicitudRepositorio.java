package Infraestructura.Repositorios;

import SolicitudEliminar.SolicitudEliminar;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SolicitudRepositorio{
    private static final SolicitudRepositorio instance = new SolicitudRepositorio();
    private List<SolicitudEliminar> solicitudes = new ArrayList<>();

    private SolicitudRepositorio() {
    }

    public static SolicitudRepositorio getInstancia() {
        return instance;
    }

    public void guardar(SolicitudEliminar solicitud) {
        solicitudes.add(solicitud);
    }

    public ArrayList<SolicitudEliminar> obtenerTodas() {
        return new ArrayList<>(this.solicitudes);
    }

    public void eliminarPorHecho(int id_hecho) {
        solicitudes.removeIf(s -> s.getId_solicitud() == id_hecho);
    }

    public void eliminarSolicitud(SolicitudEliminar solicitud) {
        solicitudes.remove(solicitud);
    }

    public SolicitudEliminar buscarPorHecho(int id_hecho) {
        for (SolicitudEliminar s : solicitudes) {
            if (s.getId_hecho() == id_hecho){
                return s;
            }
        }
        return null;
    }

}
