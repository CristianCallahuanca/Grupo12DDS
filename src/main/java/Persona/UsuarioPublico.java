package Persona;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import SolicitudEliminar.SolicitudEliminar;

import java.util.List;

public abstract class UsuarioPublico {
    

    public SolicitudEliminar solicitarEliminarHecho(Hecho hecho, String justificacion) {
        return new SolicitudEliminar(hecho, justificacion);
    }
    // Tiene sentido que esto vaya aca ya que cualquier persona en realidad puede generar una solicitud de eliminacion
    // no especifica eso el enunciado  CREO
}
