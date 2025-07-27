package Servicios;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.Hecho;
import Infraestructura.Repositorios.ColeccionRepositorio;
import Infraestructura.Repositorios.HechoRepositorio;
import Infraestructura.Repositorios.SolicitudRepositorio;
import SolicitudEliminar.SolicitudEliminar;

import java.io.IOException;

public class ServicioIdentificadorDeObjetos {

    private static final ServicioIdentificadorDeObjetos instance = new ServicioIdentificadorDeObjetos();
    public static ServicioIdentificadorDeObjetos getInstancia() {return instance;}

    public Hecho obtenerHechoPorID(int id) throws IOException {

        for(Hecho hecho: HechoRepositorio.getInstancia().obtenerTodosLosHechosDelSistema()){

            if(hecho.getId_hecho() == id) {
                return hecho;
            }
        }
        return null;
    }

    public Coleccion obtenerColeccionPorHandle(String handle) throws IOException {
        for(Coleccion c : ColeccionRepositorio.getInstancia().getColecciones()){
            if (c.getHandle().equals(handle)) {
                return c;
            }
        }
        return null;
    }

    public SolicitudEliminar obtenerSolicitudEliminar(int id) throws IOException {
        for (SolicitudEliminar s : SolicitudRepositorio.getInstancia().getSolicitudes()) {
            if (s.getId_solicitud() == id) {
                return s;
            }
        }
        return null;
    }
}
