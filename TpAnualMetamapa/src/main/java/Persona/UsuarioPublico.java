package Persona;

import AdministracionDeHechos.Coleccion;
import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.Hecho;

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

}
