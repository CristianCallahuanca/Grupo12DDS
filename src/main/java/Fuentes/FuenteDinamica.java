package Fuentes;

import AdministracionDeHechos.Hecho;
import Infraestructura.Repositorios.FuenteDinamicaRepositoryEnMemoria;

public class FuenteDinamica extends Fuente {


    public void agregarHecho(Hecho hecho) {
        if (hecho == null || hecho.getTitulo() == null) return;

        // Verificar si ya existe un hecho con el mismo titulo
        hechos.removeIf(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));

        this.hechos.add(hecho);
        this.cargarEnRepository(hecho);
    }
// Cargar en el Repository el hecho que le llega
    public void cargarEnRepository(Hecho hecho) { FuenteDinamicaRepositoryEnMemoria.getInstancia().guardar(hecho); }


}
