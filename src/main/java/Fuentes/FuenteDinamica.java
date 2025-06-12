package Fuentes;

import AdministracionDeHechos.CriterioPertenencia.CriterioDePertenencia;
import AdministracionDeHechos.CriterioPertenencia.PorOrigen;
import AdministracionDeHechos.Hecho;
import Infraestructura.Repositorios.HechoRepositoryEnMemoria;

import java.util.List;

import static AdministracionDeHechos.Origen.DINAMICA;

public class FuenteDinamica extends Fuente {

    public void cargarHechos() {
         this.hechos = HechoRepositoryEnMemoria.getInstancia().obtenerTodas()
                 .stream().filter(unHecho -> unHecho.filtarHecho(List.of(new PorOrigen(DINAMICA)))).toList();

    }

    @Override
    public List<Hecho> filtrarHechos(List<CriterioDePertenencia> criterios){
        this.cargarHechos();
        return hechos.stream().filter(unHecho -> unHecho.filtarHecho(criterios))
                .toList();

    }
    /*public void agregarHecho(Hecho hecho) {
        if (hecho == null || hecho.getTitulo() == null) return;

        // Verificar si ya existe un hecho con el mismo titulo
        hechos.removeIf(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));

        this.hechos.add(hecho);
        this.cargarEnRepository(hecho); */

// Obtener del Repository
    //public void obtenerHecho


}
