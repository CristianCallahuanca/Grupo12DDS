package Infraestructura.Repositorios;

import AdministracionDeHechos.Hecho;
import SolicitudEliminar.SolicitudEliminar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {
    void guardar(SolicitudEliminar solicitud);
    public ArrayList<SolicitudEliminar> obtenerTodas();
    void eliminarPorHecho(int id_hecho);
    public void eliminarSolicitud(SolicitudEliminar solicitud);
    SolicitudEliminar buscarPorHecho(int id_hecho);
}
