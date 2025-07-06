package AdministracionDeHechos.Consenso;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Servicios.ServicioDeAgregacion;

import java.util.List;

public abstract class AlgoritmoDeConsenso {
    /*Cuando un atributo en Java es declarado con el modificador protected significa que su visibilidad está limitada a:
    La misma clase donde fue definido.
    Las clases hijas (subclases), incluso si están en otro paquete.*/
    public List<Hecho> hechosConsensuados;

    public List<Fuente> obtenerFuentesDelSistema(){
        return ServicioDeAgregacion.getInstancia().getFuentes();
    }

    //public void verificar(List<Hecho> hechos) {}

    public void verificar(List<Hecho> hechos) {
        List<Fuente> todasLasFuentes = obtenerFuentesDelSistema();

        hechosConsensuados.clear();

        List<Hecho> filtrados = hechos.stream()
            .filter(h -> esConsensuado(h, todasLasFuentes))
            .toList();

        hechosConsensuados.addAll(filtrados);
    }

    public boolean esConsensuado(Hecho hecho, List<Fuente> fuentes){return false;};

}
