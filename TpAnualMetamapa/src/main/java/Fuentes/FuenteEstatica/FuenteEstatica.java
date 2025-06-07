package Fuentes.FuenteEstatica;

import AdministracionDeHechos.Hecho;
import Fuentes.Fuente;

import java.util.ArrayList;
import java.util.List;

public class FuenteEstatica extends Fuente {
    private Dataset dataset;

    public FuenteEstatica(Dataset dataset) {
        this.dataset = dataset;
        this.hechos = new ArrayList<>();
    }

    public void importarDesdeCSV(String rutaArchivo) {
        List<Hecho> nuevosHechos = dataset.procesarHechosDesdeCSV(rutaArchivo);

        for (Hecho nuevo : nuevosHechos) {

            hechos.removeIf(h -> h.getTitulo().equals(nuevo.getTitulo()));
            hechos.add(nuevo);
        }
    }
}

