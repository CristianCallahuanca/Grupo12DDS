package Infraestructura.Repositorios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.Hecho;
import SolicitudEliminar.SolicitudEliminar;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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
    public ArrayList<SolicitudEliminar> obtenerTodas() {
        return new ArrayList<>(this.solicitudes);
    }

    @Override
    public void eliminarPorHecho(int id_hecho) {
        solicitudes.removeIf(s -> s.getId_solicitud() == id_hecho);
    }
    @Override
    public void eliminarSolicitud(SolicitudEliminar solicitud) {
        solicitudes.remove(solicitud);
    }

    @Override
    public SolicitudEliminar buscarPorHecho(int id_hecho) {
        for (SolicitudEliminar s : solicitudes) {
            if (s.getId_hecho() == id_hecho){
                return s;
            }
        }
        return null;
    }

}
