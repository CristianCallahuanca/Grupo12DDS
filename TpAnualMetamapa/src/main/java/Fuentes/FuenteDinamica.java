package Fuentes;

import AdministracionDeHechos.Hecho;

public class FuenteDinamica extends Fuente {


    public void agregarHecho(Hecho hecho) {
        if (hecho == null || hecho.getTitulo() == null) return;

        // Verificar si ya existe un hecho con el mismo titulo
        hechos.removeIf(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()));

        this.hechos.add(hecho);
    }



}
