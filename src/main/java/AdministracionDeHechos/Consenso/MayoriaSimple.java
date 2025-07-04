package AdministracionDeHechos.Consenso;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;
import Servicios.ServicioDeAgregacion;

import java.util.List;

public class MayoriaSimple extends AlgoritmoDeConsenso{
    @Override
    public List<Hecho> verificar(List<Hecho> hechos){
        List<Fuente> todasLasFuentes = ServicioDeAgregacion.getInstancia().getFuentes();
        int mitadDeFuentes = todasLasFuentes.size()/2;

        return hechos.stream()
                .filter(h->todasLasFuentes.stream()
                        .filter(f -> f.getHechos().contains(h))
                        .count() >= mitadDeFuentes).toList();

    }
}
