package Persona;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;
import SolicitudEliminar.SolicitudEliminar;

import java.util.List;

public class UsuarioPublico {

    public List<Hecho> visualizarTodosLosHechos(Coleccion coleccion) {
        return coleccion.obtenerHechos();
    }

    public List<Hecho> visualizarHechosFiltrados(Coleccion coleccion, List<CriterioDePertenencia> filtros) {
        return coleccion.obtenerHechos().stream()
                .filter(h -> filtros.stream().allMatch(c -> c.cumpleUno(h)))
                .toList();
    }

    public SolicitudEliminar solicitarEliminarHecho(Hecho hecho, String justificacion) {
        return new SolicitudEliminar(hecho, justificacion);
    }
    // Tiene sentido que esto vaya aca ya que cualquier persona en realidad puede generar una solicitud de eliminacion
    // no especifica eso el enunciado  CREO
}
