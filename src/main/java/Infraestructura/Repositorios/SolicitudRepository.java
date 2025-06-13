package Infraestructura.Repositorios;

import AdministracionDeHechos.Hecho;
import SolicitudEliminar.SolicitudEliminar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {
    void guardar(SolicitudEliminar solicitud);
    SolicitudEliminar buscarPorHecho(Hecho hecho);
    public ArrayList<SolicitudEliminar> obtenerTodas();
    void eliminarPorHecho(Hecho hecho);
}
